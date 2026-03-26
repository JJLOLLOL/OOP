package models.furniture;

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
import types.Gender;

import org.junit.jupiter.api.Test;

class FurnitureActionTest {

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void constructorStoresAllFields() {
        FurnitureAction action = new FurnitureAction(
                "Study",
                "Improve logic",
                Map.of(NeedType.ENERGY, -5.0),
                Map.of(SkillType.LOGIC, 10.0),
                20.0,
                2.5
        );

        assertEquals("Study", action.getName());
        assertEquals("Improve logic", action.getDescription());
        assertEquals(2.5, action.getTimeRequired());
        assertEquals(20.0, action.moneyDeducted());
        assertEquals(Map.of(NeedType.ENERGY, -5.0), action.affectedNeedsByActionMap());
        assertEquals(Map.of(SkillType.LOGIC, 10.0), action.affectedSkillsByActionMap());
    }

    @Test
    void performReturnsFalseForNullCharacter() {
        FurnitureAction action = new FurnitureAction(
                "Study",
                "Improve logic",
                Map.of(),
                Map.of(),
                0.0,
                1.0
        );

        assertFalse(action.perform(null));
        assertFalse(action.perform(null, new GameClock()));
    }

    @Test
    void performFailsWhenCannotAffordCost() {
        SimCharacter sim = createSim();
        FurnitureAction action = new FurnitureAction(
                "Luxury Meal",
                "Expensive food",
                Map.of(NeedType.HUNGER, 20.0),
                Map.of(),
                2000.0,
                1.0
        );

        assertFalse(action.perform(sim));
        assertEquals(1000.0, sim.getMoney());
        assertEquals(80.0, sim.getNeed(NeedType.HUNGER).getValue());
    }

    @Test
    void performFailsWhenNegativeNeedWouldDropBelowZero() {
        SimCharacter sim = createSim();
        sim.adjustNeed(NeedType.ENERGY, -75.0);
        FurnitureAction action = new FurnitureAction(
                "Overwork",
                "Too tiring",
                Map.of(NeedType.ENERGY, -10.0),
                Map.of(),
                0.0,
                1.0
        );

        assertFalse(action.perform(sim));
        assertEquals(5.0, sim.getNeed(NeedType.ENERGY).getValue());
    }

    @Test
    void performAppliesCostNeedsAndSkillsWithoutClock() {
        SimCharacter sim = createSim();
        FurnitureAction action = new FurnitureAction(
                "Code",
                "Practice programming",
                Map.of(NeedType.ENERGY, -10.0, NeedType.FUN, 5.0),
                Map.of(SkillType.PROGRAMMING, 100.0),
                25.0,
                2.0
        );

        assertTrue(action.perform(sim));
        assertEquals(975.0, sim.getMoney());
        assertEquals(70.0, sim.getNeed(NeedType.ENERGY).getValue());
        assertEquals(85.0, sim.getNeed(NeedType.FUN).getValue());
        assertEquals(2, sim.getStats().getSkillLevel(SkillType.PROGRAMMING));
    }

    @Test
    void performAdvancesClockWhenProvided() {
        SimCharacter sim = createSim();
        GameClock clock = new GameClock();
        FurnitureAction action = new FurnitureAction(
                "Sleep",
                "Recover energy",
                Map.of(NeedType.ENERGY, 10.0),
                Map.of(),
                0.0,
                3.0
        );

        int beforeHours = clock.getHours();
        int beforeMinutes = clock.getMinutes();

        boolean result = action.perform(sim, clock);

        assertTrue(result);
        assertEquals(beforeHours + 3, clock.getHours());
        assertEquals(beforeMinutes, clock.getMinutes());
    }

    @Test
    void performDoesNotAdvanceClockWhenTimeIsZero() {
        SimCharacter sim = createSim();
        GameClock clock = new GameClock();
        FurnitureAction action = new FurnitureAction(
                "Instant",
                "No time",
                Map.of(),
                Map.of(),
                0.0,
                0.0
        );

        int beforeHours = clock.getHours();
        int beforeMinutes = clock.getMinutes();
        int beforeDays = clock.getDays();

        boolean result = action.perform(sim, clock);

        assertTrue(result);
        assertEquals(beforeDays, clock.getDays());
        assertEquals(beforeHours, clock.getHours());
        assertEquals(beforeMinutes, clock.getMinutes());
    }
}