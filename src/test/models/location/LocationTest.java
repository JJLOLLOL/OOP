package models.location;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import models.furniture.Furniture;

class LocationTest {

    private static class TestLocation extends Location {
        TestLocation(String locationName, ArrayList<Furniture> furnitures) {
            super(locationName, furnitures);
        }

        void addFurnitureForTest(Furniture furniture) {
            addFurnitureInternal(furniture);
        }

        boolean removeFurnitureForTest(Furniture furniture) {
            return removeFurnitureInternal(furniture);
        }

        void replaceFurnitureForTest(List<Furniture> furnitures) {
            replaceFurnitureInternal(furnitures);
        }
    }

    private Furniture makeFurniture(String name) {
        return new Furniture(name, name + " desc", 50.0);
    }

    @Test
    void constructor_setsNameAndFurnitureList() {
        ArrayList<Furniture> furnitures = new ArrayList<>();
        Furniture chair = makeFurniture("Chair");
        furnitures.add(chair);

        Location location = new Location("Park", furnitures);

        assertEquals("Park", location.getLocationName());
        assertTrue(location.containsFurniture(chair));
        assertEquals(1, location.getFurnitureViews().size());
        assertTrue(location.getNpcViews().isEmpty());
    }

    @Test
    void constructor_withNullFurniture_createsEmptyFurnitureList() {
        Location location = new Location("Empty Place", null);

        assertEquals("Empty Place", location.getLocationName());
        assertTrue(location.getFurnitureViews().isEmpty());
        assertTrue(location.getNpcViews().isEmpty());
    }

    @Test
    void containsFurniture_returnsTrueOnlyWhenFurnitureExists() {
        Furniture bed = makeFurniture("Bed");
        Furniture table = makeFurniture("Table");

        ArrayList<Furniture> furnitures = new ArrayList<>();
        furnitures.add(bed);

        Location location = new Location("Room", furnitures);

        assertTrue(location.containsFurniture(bed));
        assertFalse(location.containsFurniture(table));
    }

    @Test
    void getFurnitureViews_returnsUnmodifiableCopy() {
        ArrayList<Furniture> furnitures = new ArrayList<>();
        furnitures.add(makeFurniture("Lamp"));

        Location location = new Location("Hall", furnitures);
        List<Furniture> furnitureViews = location.getFurnitureViews();

        assertThrows(UnsupportedOperationException.class,
                () -> furnitureViews.add(makeFurniture("Desk")));
    }

    @Test
    void getNpcViews_returnsUnmodifiableCopy() {
        Location location = new Location("Hall", new ArrayList<>());

        assertThrows(UnsupportedOperationException.class,
                () -> location.getNpcViews().add(null));
    }

    @Test
    void addFurnitureInternal_addsFurniture() {
        TestLocation location = new TestLocation("Test", new ArrayList<>());
        Furniture sofa = makeFurniture("Sofa");

        location.addFurnitureForTest(sofa);

        assertTrue(location.containsFurniture(sofa));
        assertEquals(1, location.getFurnitureViews().size());
    }

    @Test
    void addFurnitureInternal_throwsExceptionWhenFurnitureIsNull() {
        TestLocation location = new TestLocation("Test", new ArrayList<>());

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> location.addFurnitureForTest(null));

        assertEquals("Furniture cannot be null.", ex.getMessage());
    }

    @Test
    void removeFurnitureInternal_removesFurniture() {
        TestLocation location = new TestLocation("Test", new ArrayList<>());
        Furniture shelf = makeFurniture("Shelf");

        location.addFurnitureForTest(shelf);
        boolean result = location.removeFurnitureForTest(shelf);

        assertTrue(result);
        assertFalse(location.containsFurniture(shelf));
        assertEquals(0, location.getFurnitureViews().size());
    }

    @Test
    void removeFurnitureInternal_throwsExceptionWhenFurnitureIsNull() {
        TestLocation location = new TestLocation("Test", new ArrayList<>());

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> location.removeFurnitureForTest(null));

        assertEquals("Furniture cannot be null.", ex.getMessage());
    }

    @Test
    void replaceFurnitureInternal_replacesAllFurniture() {
        TestLocation location = new TestLocation("Test", new ArrayList<>());
        Furniture oldItem = makeFurniture("Old");
        Furniture newItem1 = makeFurniture("New 1");
        Furniture newItem2 = makeFurniture("New 2");

        location.addFurnitureForTest(oldItem);
        location.replaceFurnitureForTest(List.of(newItem1, newItem2));

        assertEquals(2, location.getFurnitureViews().size());
        assertFalse(location.containsFurniture(oldItem));
        assertTrue(location.containsFurniture(newItem1));
        assertTrue(location.containsFurniture(newItem2));
    }

    @Test
    void replaceFurnitureInternal_throwsExceptionWhenListIsNull() {
        TestLocation location = new TestLocation("Test", new ArrayList<>());

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> location.replaceFurnitureForTest(null));

        assertEquals("Furniture list cannot be null.", ex.getMessage());
    }
}