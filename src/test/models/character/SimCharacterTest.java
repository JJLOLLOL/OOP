package models.character;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import core.ActionResult;
import core.GameClock;
import models.career.CareerList;
import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.location.House;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import types.Gender;

class SimCharacterTest {

    private SimCharacter createSim() {
        return new SimCharacter(
                "Taylor",
                21,
                Gender.FEMALE,
                new Location("Home", new ArrayList<>()));
    }

    private House makeHouse(String name, double price, int tier) {
        return new House(name, new ArrayList<>(), price, 1.0, tier);
    }

    private Furniture makeFurniture(String name, double price) {
        return new Furniture(name, name + " desc", price);
    }

    private FurnitureAction makeWorkAction(
            Map<NeedType, Double> affectedNeeds,
            Map<SkillType, Double> affectedSkills) {
        return new FurnitureAction(
                "Work",
                "Work shift",
                affectedNeeds,
                affectedSkills,
                0.0,
                0.0);
    }

    @AfterEach
    void resetStaticWorkAction() {
        SimCharacter.setWorkAction(null);
    }

    @Test
    void constructorCreatesDefaultSystems() {
        SimCharacter sim = createSim();

        assertEquals("Taylor", sim.getName());
        assertEquals(21, sim.getAge());
        assertEquals(1000.0, sim.getMoney());
        assertEquals(CareerList.JOBLESS, sim.getCareer().getCurrentCareer());
        assertTrue(sim.isJobless());

        assertEquals(80.0, sim.getNeed(NeedType.HUNGER).getValue());
        assertEquals(80.0, sim.getNeed(NeedType.HYGIENE).getValue());
        assertEquals(80.0, sim.getNeed(NeedType.ENERGY).getValue());
        assertEquals(80.0, sim.getNeed(NeedType.FUN).getValue());
        assertEquals(80.0, sim.getNeed(NeedType.SOCIAL).getValue());

        assertTrue(sim.getStats().getSkillViews().size() > 0);
    }

    @Test
    void adjustSkillXpAndNeedRejectNullAndHandleZero() {
        SimCharacter sim = createSim();

        assertThrows(IllegalArgumentException.class, () -> sim.adjustSkillXp(null, 5.0));
        assertThrows(IllegalArgumentException.class, () -> sim.adjustNeed(null, 5.0));

        assertEquals(0, sim.adjustSkillXp(SkillType.LOGIC, 0.0));
        sim.adjustNeed(NeedType.ENERGY, 0.0);

        assertEquals(80.0, sim.getNeed(NeedType.ENERGY).getValue());
        assertEquals(0.0, sim.getStats().getSkillXp(SkillType.LOGIC));
    }

    @Test
    void adjustSkillXpAndNeedUpdateUnderlyingStats() {
        SimCharacter sim = createSim();

        int levels = sim.adjustSkillXp(SkillType.LOGIC, 100.0);
        sim.adjustNeed(NeedType.HUNGER, -15.0);

        assertEquals(1, levels);
        assertEquals(2, sim.getStats().getSkillLevel(SkillType.LOGIC));
        assertEquals(65.0, sim.getNeed(NeedType.HUNGER).getValue());
    }

    @Test
    void criticalHungerReducesPositiveEnergyRecovery() {
        SimCharacter sim = createSim();

        sim.adjustNeed(NeedType.HUNGER, -70.0);
        sim.adjustNeed(NeedType.ENERGY, 10.0);

        assertEquals(10.0, sim.getNeed(NeedType.HUNGER).getValue());
        assertEquals(85.0, sim.getNeed(NeedType.ENERGY).getValue());
    }

    @Test
    void financeCareerAndHousingMethodsDelegateCorrectly() {
        SimCharacter sim = createSim();
        House house = new House("Starter", new ArrayList<>());

        sim.earnMoney(200.0);
        sim.spendMoney(150.0);
        sim.joinCareer(CareerList.CHEF);
        sim.assignHouse(house);

        assertEquals(1050.0, sim.getMoney());
        assertTrue(sim.canAfford(1000.0));
        assertEquals(CareerList.CHEF, sim.getCareer().getCurrentCareer());
        assertFalse(sim.isJobless());
        assertSame(house, sim.getCurrentHouse());
    }

    @Test
    void joinCareerRejectsNull() {
        SimCharacter sim = createSim();

        assertThrows(IllegalArgumentException.class, () -> sim.joinCareer(null));
    }

    @Test
    void updateNeedsDecaysAllNeedsUsingBaseDecayRates() {
        SimCharacter sim = createSim();

        sim.updateNeeds(1.0);

        assertEquals(72.0, sim.getNeed(NeedType.HUNGER).getValue());
        assertEquals(77.0, sim.getNeed(NeedType.HYGIENE).getValue());
        assertEquals(72.0, sim.getNeed(NeedType.ENERGY).getValue());
        assertEquals(77.0, sim.getNeed(NeedType.FUN).getValue());
        assertEquals(77.0, sim.getNeed(NeedType.SOCIAL).getValue());
    }

    @Test
    void workFailsWhenJobless() {
        SimCharacter sim = createSim();
        GameClock clock = new GameClock();

        ActionResult result = sim.work(clock);

        assertFalse(result.isSuccess());
        assertEquals("You need a job before you can work!", result.getMessage());
    }

    @Test
    void workFailsBeforeShiftStarts() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        GameClock clock = new GameClock(); // 08:00

        ActionResult result = sim.work(clock);

        assertFalse(result.isSuccess());
        assertEquals("Work doesn't start until 09:00.", result.getMessage());
    }

    @Test
    void workFailsAfterShiftEnds() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        GameClock clock = new GameClock();
        clock.advanceHours(12.0); // 20:00

        ActionResult result = sim.work(clock);

        assertFalse(result.isSuccess());
        assertEquals("he work day is over (shift ends 19:00). Come back tomorrow!", result.getMessage());
    }

    @Test
    void workThrowsWhenWorkActionIsNotInitialized() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        GameClock clock = new GameClock();
        clock.advanceHours(1.0); // 09:00

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> sim.work(clock));
        assertEquals(
                "Work action has not been initialized. Ensure DataParser has set this value.",
                ex.getMessage());
    }

    @Test
    void workSucceedsAndAppliesPayNeedsSkillXpAndTimeAdvance() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        Map<NeedType, Double> needEffects = new EnumMap<>(NeedType.class);
        needEffects.put(NeedType.ENERGY, -10.0);
        needEffects.put(NeedType.HUNGER, -5.0);

        SimCharacter.setWorkAction(makeWorkAction(needEffects, new EnumMap<>(SkillType.class)));

        GameClock clock = new GameClock();
        clock.advanceHours(1.0); // 09:00

        ActionResult result = sim.work(clock);

        assertTrue(result.isSuccess());
        assertEquals("Worked 10.0 / 10 hours. Earned $75.00.", result.getMessage());

        assertEquals(1075.0, sim.getMoney());
        assertEquals(70.0, sim.getNeed(NeedType.ENERGY).getValue());
        assertEquals(75.0, sim.getNeed(NeedType.HUNGER).getValue());

        assertEquals(50.0, sim.getStats().getSkillXp(SkillType.COOKING));
        assertEquals(50.0, sim.getStats().getSkillXp(SkillType.CREATIVITY));

        assertEquals(19, clock.getHours());
        assertEquals(0, clock.getMinutes());
    }

    @Test
    void workCanTriggerPromotionMessage() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        SimCharacter.setWorkAction(
                makeWorkAction(new EnumMap<>(NeedType.class), new EnumMap<>(SkillType.class)));

        ActionResult lastResult = null;

        for (int i = 0; i < 5; i++) {
            GameClock clock = new GameClock();
            clock.advanceHours(1.0); // 09:00
            lastResult = sim.work(clock);
        }

        assertNotNull(lastResult);
        assertTrue(lastResult.isSuccess());
        assertTrue(lastResult.getMessage().contains("Promoted to Junior Employee!"));
        assertEquals(2, sim.getCareer().getCurrentRank());
        assertEquals("Junior Employee", sim.getCareer().getRank());
    }

    @Test
    void purchaseHouseFailsWhenInsufficientFunds() {
        SimCharacter sim = createSim();
        sim.assignHouse(makeHouse("Starter", 0.0, 1));

        House villa = makeHouse("Villa", 5000.0, 3);

        ActionResult result = sim.purchaseHouse(villa);

        assertFalse(result.isSuccess());
        assertEquals("Insufficient funds! Need $5000.0, have: $1000.0", result.getMessage());
        assertEquals(1000.0, sim.getMoney());
        assertEquals(1, sim.getCurrentHouse().getTier());
    }

    @Test
    void purchaseHouseSucceedsAndUpgradesCurrentHouse() {
        SimCharacter sim = createSim();
        House starter = makeHouse("Starter", 0.0, 1);
        House upgrade = makeHouse("Upgrade", 600.0, 4);

        sim.assignHouse(starter);

        ActionResult result = sim.purchaseHouse(upgrade);

        assertTrue(result.isSuccess());
        assertEquals("Taylor bought Upgrade for $600.0", result.getMessage());
        assertEquals(400.0, sim.getMoney());
        assertEquals(4, sim.getCurrentHouse().getTier());
        assertEquals(600.0, sim.getCurrentHouse().getPrice());
    }

    @Test
    void buyFurnitureFailsWhenHouseIsFull() {
        SimCharacter sim = createSim();
        House house = makeHouse("Starter", 0.0, 1);
        sim.assignHouse(house);

        for (int i = 1; i <= house.getMaxFurnitureCapacity(); i++) {
            house.addFurniture(makeFurniture("Furniture " + i, 10.0));
        }

        ActionResult result = sim.buyFurniture(makeFurniture("Extra", 100.0));

        assertFalse(result.isSuccess());
        assertEquals("Your house is at maximum furniture capacity.", result.getMessage());
        assertEquals(1000.0, sim.getMoney());
    }

    @Test
    void buyFurnitureFailsWhenInsufficientFunds() {
        SimCharacter sim = createSim();
        sim.assignHouse(makeHouse("Starter", 0.0, 1));

        Furniture luxury = makeFurniture("Luxury Bed", 2000.0);

        ActionResult result = sim.buyFurniture(luxury);

        assertFalse(result.isSuccess());
        assertEquals("Insufficient funds! Need $2000.0, have: $1000.0", result.getMessage());
        assertEquals(1000.0, sim.getMoney());
    }

    @Test
    void buyFurnitureSucceeds() {
        SimCharacter sim = createSim();
        House house = makeHouse("Starter", 0.0, 1);
        sim.assignHouse(house);

        Furniture chair = makeFurniture("Chair", 200.0);

        ActionResult result = sim.buyFurniture(chair);

        assertTrue(result.isSuccess());
        assertEquals("Taylor bought Chair for $200.0", result.getMessage());
        assertEquals(800.0, sim.getMoney());
        assertTrue(sim.getCurrentHouse().containsFurniture(chair));
    }

    @Test
    void sellFurnitureFailsWhenHouseIsEmpty() {
        SimCharacter sim = createSim();
        sim.assignHouse(makeHouse("Starter", 0.0, 1));

        ActionResult result = sim.sellFurniture(makeFurniture("Chair", 200.0));

        assertFalse(result.isSuccess());
        assertEquals("Your house is empty.", result.getMessage());
        assertEquals(1000.0, sim.getMoney());
    }

    @Test
    void sellFurnitureSucceedsAndAddsHalfPriceToMoney() {
        SimCharacter sim = createSim();
        House house = makeHouse("Starter", 0.0, 1);
        Furniture chair = makeFurniture("Chair", 200.0);

        sim.assignHouse(house);
        house.addFurniture(chair);

        ActionResult result = sim.sellFurniture(chair);

        assertTrue(result.isSuccess());
        assertEquals("Taylor sold Chair for $200.0", result.getMessage());
        assertEquals(1100.0, sim.getMoney());
        assertFalse(sim.getCurrentHouse().containsFurniture(chair));
    }

    @Test
    void getPurchaseMessageFormatsSuccessAndFailureCases() {
        SimCharacter sim = createSim();
        House house = new House("Villa", new ArrayList<>(), 5000.0, 2.0, 3);

        assertEquals(
                "Insufficient funds! You need $5000.0 to purchase this house.",
                sim.getPurchaseMessage(house, false));
        assertEquals(
                "Taylor purchased Villa for $5000.0!",
                sim.getPurchaseMessage(house, true));
    }
}