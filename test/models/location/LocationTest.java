package models.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import models.actions.Furniture;
import models.character.NPCCharacter;
import org.junit.jupiter.api.Test;

class LocationTest {

    private static class TestLocation extends Location {
        TestLocation(String locationName, ArrayList<Furniture> furnitures) {
            super(locationName, furnitures);
        }

        void addFurnitureForTest(Furniture furniture) {
            addFurnitureInternal(furniture);
        }

        void removeFurnitureForTest(Furniture furniture) {
            removeFurnitureInternal(furniture);
        }

        void replaceFurnitureForTest(List<Furniture> furnitures) {
            replaceFurnitureInternal(furnitures);
        }
    }

    @Test
    void constructorCreatesEmptyFurnitureListWhenInputIsNull() {
        Location location = new Location("Park", null);

        assertEquals("Park", location.getLocationName());
        assertEquals(0, location.getFurnitureViews().size());
    }

    @Test
    void furnitureViewIsUnmodifiable() {
        ArrayList<Furniture> furnitures = new ArrayList<>();
        furnitures.add(new Furniture("Sofa", "Comfortable seat", 200));
        Location location = new Location("Living Room", furnitures);

        List<Furniture> furnitureView = location.getFurnitureViews();

        assertEquals(1, furnitureView.size());
        assertThrows(UnsupportedOperationException.class,
                () -> furnitureView.add(new Furniture("TV", "Large screen", 500)));
    }

    @Test
    void npcViewStartsEmpty() {
        Location location = new Location("Kitchen", new ArrayList<>());

        assertEquals(0, location.getNpcViews().size());
    }

    @Test
    void npcCharactersCanBeAddedAndRemoved() {
        Location home = new Location("Home", new ArrayList<>());
        TreeMap<Integer, Location> schedule = new TreeMap<>();
        schedule.put(900, home);
        NPCCharacter npc = new NPCCharacter("Alex", 25, "Non-binary", "Friendly neighbor", schedule);

        home.addNpcCharacter(npc);
        assertEquals(1, home.getNpcViews().size());

        home.removeNpcCharacter(npc);
        assertEquals(0, home.getNpcViews().size());
    }

    @Test
    void npcViewIsUnmodifiable() {
        Location home = new Location("Home", new ArrayList<>());
        TreeMap<Integer, Location> schedule = new TreeMap<>();
        schedule.put(900, home);
        NPCCharacter npc = new NPCCharacter("Alex", 25, "Non-binary", "Friendly neighbor", schedule);
        home.addNpcCharacter(npc);

        List<NPCCharacter> npcView = home.getNpcViews();

        assertThrows(UnsupportedOperationException.class, () -> npcView.remove(0));
    }

    @Test
    void internalFurnitureMutatorsValidateInputAndUpdateState() {
        TestLocation location = new TestLocation("Study", new ArrayList<>());
        Furniture desk = new Furniture("Desk", "Study desk", 200);
        Furniture lamp = new Furniture("Lamp", "Desk lamp", 40);

        assertThrows(IllegalArgumentException.class, () -> location.addFurnitureForTest(null));
        assertThrows(IllegalArgumentException.class, () -> location.removeFurnitureForTest(null));
        assertThrows(IllegalArgumentException.class, () -> location.replaceFurnitureForTest(null));

        location.addFurnitureForTest(desk);
        assertEquals(1, location.getFurnitureViews().size());

        location.replaceFurnitureForTest(List.of(lamp));
        assertEquals(1, location.getFurnitureViews().size());
        assertEquals("Lamp", location.getFurnitureViews().get(0).getName());

        location.removeFurnitureForTest(lamp);
        assertEquals(0, location.getFurnitureViews().size());
    }
}
