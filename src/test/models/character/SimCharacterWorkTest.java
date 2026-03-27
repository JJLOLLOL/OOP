package models.character;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.ActionResult;
import core.GameClock;
import models.career.CareerList;
import models.furniture.FurnitureAction;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import types.Gender;

class SimCharacterWorkTest {

    @BeforeEach
    void resetWorkAction() throws Exception {
        Field workActionField = SimCharacter.class.getDeclaredField("WORK_ACTION");
        workActionField.setAccessible(true);
        workActionField.set(null, null);
    }

    private SimCharacter createSim() {
        return new SimCharacter(
                "Taylor",
                21,
                Gender.FEMALE,
                new Location("Home", new ArrayList<>()));
    }

    private FurnitureAction makeWorkAction(Map<NeedType, Double> affectedNeeds) {
        return new FurnitureAction(
                "Work",
                "Work shift",
                affectedNeeds,
                new EnumMap<>(SkillType.class),
                0.0,
                0.0);
    }

    @Test
    void workFailsWhenJobless() {
        SimCharacter sim = createSim();
        GameClock clock = new GameClock();

        ActionResult result = sim.work(clock);

        assertFalse(result.isSuccess());
        assertEquals("You need a job before you can work!", result.getMessage());
        assertEquals(1000.0, sim.getMoney());
        assertEquals(8, clock.getHours());
        assertEquals(0, clock.getMinutes());
    }

    @Test
    void workFailsBeforeShiftStarts() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        GameClock clock = new GameClock(); // 08:00

        ActionResult result = sim.work(clock);

        assertFalse(result.isSuccess());
        assertEquals("Work doesn't start until 09:00.", result.getMessage());
        assertEquals(1000.0, sim.getMoney());
        assertEquals(8, clock.getHours());
        assertEquals(0, clock.getMinutes());
        assertEquals(1, sim.getCareer().getCurrentRank());
        assertEquals(0.0, sim.getCareer().getProgress());
    }

    @Test
    void workFailsAfterShiftEnds() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        GameClock clock = new GameClock();
        clock.advanceHours(12.0); // 20:00

        ActionResult result = sim.work(clock);

        assertFalse(result.isSuccess());
        assertEquals("The work day is over (shift ends 19:00). Come back tomorrow!", result.getMessage());
        assertEquals(1000.0, sim.getMoney());
        assertEquals(20, clock.getHours());
        assertEquals(0, clock.getMinutes());
        assertEquals(1, sim.getCareer().getCurrentRank());
        assertEquals(0.0, sim.getCareer().getProgress());
    }

    @Test
    void workMidShiftAppliesPartialPayNeedChangesAndEventuallyPromotes() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        Map<NeedType, Double> needEffects = new EnumMap<>(NeedType.class);
        needEffects.put(NeedType.ENERGY, -10.0);
        needEffects.put(NeedType.HUNGER, -6.0);
        SimCharacter.setWorkAction(makeWorkAction(needEffects));

        GameClock firstShiftClock = new GameClock();
        firstShiftClock.advanceHours(6.0); // 14:00, 5 hours left in a 10 hour shift

        ActionResult firstResult = sim.work(firstShiftClock);
        
        assertTrue(firstResult.isSuccess());
        assertEquals("Worked 5.0 / 10 hours. Earned $37.50.", firstResult.getMessage());
        assertEquals(1037.5, sim.getMoney());
        assertEquals(75.0, sim.getNeed(NeedType.ENERGY).getValue());
        assertEquals(77.0, sim.getNeed(NeedType.HUNGER).getValue());
        assertEquals(25.0, sim.getStats().getSkillXp(SkillType.COOKING));
        assertEquals(25.0, sim.getStats().getSkillXp(SkillType.CREATIVITY));
        assertEquals(19, firstShiftClock.getHours());
        assertEquals(0, firstShiftClock.getMinutes());
        assertEquals(10.0, sim.getCareer().getProgress());
        assertEquals(1, sim.getCareer().getCurrentRank());

        ActionResult promotionResult = null;
        for (int i = 0; i < 9; i++) {
            GameClock clock = new GameClock();
            clock.advanceHours(6.0); // 14:00 each day
            promotionResult = sim.work(clock);
        }

        assertNotNull(promotionResult);
        assertTrue(promotionResult.isSuccess());
        assertEquals("Worked 5.0 / 10 hours. Earned $37.50.\nPromoted to Junior Employee!", promotionResult.getMessage());
        assertEquals(2, sim.getCareer().getCurrentRank());
        assertEquals("Junior Employee", sim.getCareer().getRank());
        assertEquals(0.0, sim.getCareer().getProgress());
        assertEquals(1375.0, sim.getMoney());
    }

    @Test
    void workThrowsWhenWorkActionHasNotBeenInitialized() {
        SimCharacter sim = createSim();
        sim.joinCareer(CareerList.CHEF);

        GameClock clock = new GameClock();
        clock.advanceHours(1.0); // 09:00, inside shift so guard is reached

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> sim.work(clock));

        assertEquals("Work action has not been initialized. Ensure WorldLoader has set this value.", exception.getMessage());
        assertEquals(1000.0, sim.getMoney());
        assertEquals(19, clock.getHours());
        assertEquals(0, clock.getMinutes());
        assertEquals(0.0, sim.getCareer().getProgress());
    }
}
