package models.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.GameClock;
import java.util.ArrayList;
import java.util.Map;
import models.character.SimCharacter;
import models.location.Location;
import org.junit.jupiter.api.Test;

class FurnitureTest {

    private SimCharacter createSim() {
        return new SimCharacter("Avery", 20, "M", new Location("Room", new ArrayList<>()));
    }

    @Test
    void furnitureStoresMetadataAndIgnoresNullAction() {
        Furniture furniture = new Furniture("Desk", "Study desk", 200.0);

        furniture.addAction(null);

        assertEquals("Desk", furniture.getName());
        assertEquals("Study desk", furniture.getDescription());
        assertEquals(200.0, furniture.getPrice());
        assertTrue(furniture.getActions().isEmpty());
        assertTrue(furniture.getActionNames().isEmpty());
    }

    @Test
    void furnitureCanRetrieveAndPerformNamedAction() {
        Furniture furniture = new Furniture("Desk", "Study desk", 200.0);
        FurnitureAction action = new FurnitureAction(
                "WORK",
                "Get some logic practice.",
                Map.of("ENERGY", -10.0),
                Map.of("LOGIC", 50.0),
                5.0,
                1.0);
        SimCharacter sim = createSim();
        GameClock clock = new GameClock();
        furniture.addAction(action);

        assertEquals(action, furniture.getAction("WORK"));
        assertNull(furniture.getAction("MISSING"));
        assertTrue(action.perform(sim, clock));
        assertEquals(995.0, sim.getMoney());
        assertEquals(70.0, sim.getNeed(models.need.NeedType.ENERGY).getValue());
        assertEquals(9, clock.getHours());
        assertEquals(0, clock.getMinutes());
    }

    @Test
    void performActionReturnsFalseForMissingAction() {
        Furniture furniture = new Furniture("Desk", "Study desk", 200.0);

        assertFalse(furniture.performAction("UNKNOWN", createSim()));
    }

    @Test
    void returnedActionListsAreDefensiveCopies() {
        Furniture furniture = new Furniture("Desk", "Study desk", 200.0);
        FurnitureAction action = new FurnitureAction("WORK", "desc", Map.of(), Map.of(), 0.0, 0.0);
        furniture.addAction(action);

        var names = furniture.getActionNames();
        var actions = furniture.getActions();
        names.add("X");
        actions.clear();

        assertEquals(1, furniture.getActionNames().size());
        assertEquals(1, furniture.getActions().size());
    }
}
