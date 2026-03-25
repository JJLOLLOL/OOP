package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Types.CareerList;
import java.util.ArrayList;
import models.location.House;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class SimCharacterTest {

    private SimCharacter createSim() {
        return new SimCharacter("Taylor", 21, "F", new Location("Home", new ArrayList<>()));
    }

    @Test
    void constructorCreatesDefaultSystems() {
        SimCharacter sim = createSim();

        assertEquals(1000.0, sim.getMoney());
        assertEquals(CareerList.JOBLESS, sim.getCareer().getCurrentCareer());
        assertEquals(80.0, sim.getNeed(NeedType.HUNGER).getValue());
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
    void financesCareerAndHousingMethodsDelegateCorrectly() {
        SimCharacter sim = createSim();
        House house = new House("Starter", new ArrayList<>());

        sim.earnMoney(200.0);
        sim.spendMoney(150.0);
        sim.joinCareer(CareerList.CHEF);
        sim.assignHouse(house);

        assertEquals(1050.0, sim.getMoney());
        assertTrue(sim.canAfford(1000.0));
        assertEquals(CareerList.CHEF, sim.getCareer().getCurrentCareer());
        assertEquals(house, sim.getCurrentHouse());
    }

    @Test
    void joinCareerRejectsNull() {
        SimCharacter sim = createSim();

        assertThrows(IllegalArgumentException.class, () -> sim.joinCareer(null));
    }
}
