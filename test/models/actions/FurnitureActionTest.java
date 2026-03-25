package models.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.GameClock;
import java.util.ArrayList;
import java.util.Map;
import models.character.SimCharacter;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class FurnitureActionTest {

    private SimCharacter createSim() {
        return new SimCharacter("Jordan", 19, "F", new Location("Home", new ArrayList<>()));
    }

    @Test
    void gettersExposeConfiguredFields() {
        FurnitureAction action = new FurnitureAction(
                "TRAIN",
                "Build fitness.",
                Map.of("ENERGY", -15.0),
                Map.of("FITNESS", 20.0),
                10.0,
                1.5);

        assertEquals("TRAIN", action.getName());
        assertEquals("Build fitness.", action.getDescription());
        assertEquals(10.0, action.moneyDeducted());
        assertEquals(1.5, action.getTimeRequired());
        assertEquals(-15.0, action.affectedNeedsByActionMap().get("ENERGY"));
        assertEquals(20.0, action.affectedSkillsByActionMap().get("FITNESS"));
    }

    @Test
    void performReturnsFalseForNullCharacterOrInsufficientMoney() {
        FurnitureAction action = new FurnitureAction("TRAIN", "desc", Map.of(), Map.of(), 2000.0, 1.0);

        assertFalse(action.perform(null));
        assertFalse(action.perform(createSim()));
    }

    @Test
    void performReturnsFalseWhenNegativeNeedWouldGoBelowZero() {
        SimCharacter sim = createSim();
        sim.adjustNeed(NeedType.ENERGY, -75.0);
        FurnitureAction action = new FurnitureAction(
                "TRAIN",
                "desc",
                Map.of("ENERGY", -10.0),
                Map.of(),
                0.0,
                1.0);

        assertFalse(action.perform(sim));
        assertEquals(5.0, sim.getNeed(NeedType.ENERGY).getValue());
    }

    @Test
    void performAppliesEffectsAndAdvancesClock() {
        SimCharacter sim = createSim();
        GameClock clock = new GameClock();
        FurnitureAction action = new FurnitureAction(
                "READ",
                "desc",
                Map.of("FUN", 10.0, "ENERGY", -5.0),
                Map.of("LOGIC", 100.0),
                20.0,
                1.5);

        boolean performed = action.perform(sim, clock);

        assertTrue(performed);
        assertEquals(980.0, sim.getMoney());
        assertEquals(90.0, sim.getNeed(NeedType.FUN).getValue());
        assertEquals(75.0, sim.getNeed(NeedType.ENERGY).getValue());
        assertEquals(2, sim.getStats().getSkillLevel(SkillType.LOGIC));
        assertEquals(9, clock.getHours());
        assertEquals(30, clock.getMinutes());
    }
}
