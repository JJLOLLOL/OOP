package models.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import models.character.SimCharacter;
import models.location.Location;
import org.junit.jupiter.api.Test;

class FurnitureFactoryTest {

    private SimCharacter createSim() {
        return new SimCharacter("Robin", 22, "M", new Location("Home", new ArrayList<>()));
    }

    @Test
    void cheapMattressHasExpectedMetadataAndActions() {
        Furniture mattress = FurnitureFactory.createCheapMattress();

        assertEquals("Cheap Mattress", mattress.getName());
        assertEquals(50.0, mattress.getPrice());
        assertEquals(2, mattress.getActions().size());
        assertTrue(mattress.getActionNames().contains("Nap"));
        assertTrue(mattress.getActionNames().contains("Sleep"));
    }

    @Test
    void modernStoveIncludesAllCookingActions() {
        Furniture stove = FurnitureFactory.createModernStove();

        assertEquals("Modern Stove", stove.getName());
        assertEquals(3, stove.getActions().size());
        assertNotNull(stove.getAction("Cook Ramen"));
        assertNotNull(stove.getAction("Cook Fried Rice"));
        assertNotNull(stove.getAction("Cook Steak"));
    }

    @Test
    void workDeskCreatesFullShiftAction() {
        Furniture desk = FurnitureFactory.createWorkDesk();
        FurnitureAction work = desk.getAction("Work");

        assertNotNull(work);
        assertEquals(8.0, work.getTimeRequired());
        assertEquals(0.0, work.moneyDeducted());
        assertTrue(work.affectedNeedsByActionMap().containsKey("Hunger"));
    }

    @Test
    void factoryActionPerformCurrentlyUsesDisplayNamesThatDoNotMatchEnumLookup() {
        Furniture mattress = FurnitureFactory.createCheapMattress();

        assertThrows(IllegalArgumentException.class, () -> mattress.performAction("Nap", createSim()));
    }
}
