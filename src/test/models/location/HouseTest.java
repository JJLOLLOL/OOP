package models.location;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import models.furniture.Furniture;

class HouseTest {

    private Furniture makeFurniture(String name) {
        return new Furniture(name, name + " desc", 100.0);
    }

    @Test
    void defaultConstructor_setsDefaultValues() {
        House house = new House("Starter House", new ArrayList<>());

        assertEquals("Starter House", house.getLocationName());
        assertEquals(0.0, house.getPrice());
        assertEquals(1.0, house.getRate());
        assertEquals(1, house.getTier());
        assertEquals(6, house.getMaxFurnitureCapacity());
        assertEquals(0, house.getFurnitureCount());
        assertTrue(house.canAddFurniture());
    }

    @Test
    void fullConstructor_setsProvidedValues() {
        ArrayList<Furniture> furnitures = new ArrayList<>();
        furnitures.add(makeFurniture("Bed"));

        House house = new House("Villa", furnitures, 5000.0, 2.5, 4);

        assertEquals("Villa", house.getLocationName());
        assertEquals(5000.0, house.getPrice());
        assertEquals(2.5, house.getRate());
        assertEquals(4, house.getTier());
        assertEquals(9, house.getMaxFurnitureCapacity());
        assertEquals(1, house.getFurnitureCount());
    }

    @Test
    void getMaxFurnitureCapacity_returnsCorrectValues() {
        House tierOne = new House("Tier 1", new ArrayList<>(), 1000.0, 1.0, 1);
        House tierFive = new House("Tier 5", new ArrayList<>(), 5000.0, 3.0, 5);
        House tierTen = new House("Tier 10", new ArrayList<>(), 10000.0, 5.0, 10);

        assertEquals(6, tierOne.getMaxFurnitureCapacity());
        assertEquals(10, tierFive.getMaxFurnitureCapacity());
        assertEquals(15, tierTen.getMaxFurnitureCapacity());
    }

    @Test
    void addFurniture_addsFurnitureSuccessfully() {
        House house = new House("Home", new ArrayList<>());
        Furniture chair = makeFurniture("Chair");

        house.addFurniture(chair);

        assertEquals(1, house.getFurnitureCount());
        assertTrue(house.containsFurniture(chair));
    }

    @Test
    void addFurniture_throwsExceptionWhenFurnitureIsNull() {
        House house = new House("Home", new ArrayList<>());

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> house.addFurniture(null));

        assertEquals("Furniture cannot be null.", ex.getMessage());
    }

    @Test
    void addFurniture_throwsExceptionWhenCapacityReached() {
        House house = new House("Home", new ArrayList<>());

        for (int i = 1; i <= house.getMaxFurnitureCapacity(); i++) {
            house.addFurniture(makeFurniture("Furniture " + i));
        }

        IllegalStateException ex =
                assertThrows(IllegalStateException.class,
                        () -> house.addFurniture(makeFurniture("Extra")));

        assertEquals("Furniture capacity reached.", ex.getMessage());
    }

    @Test
    void removeFurniture_removesFurnitureSuccessfully() {
        House house = new House("Home", new ArrayList<>());
        Furniture table = makeFurniture("Table");

        house.addFurniture(table);
        house.removeFurniture(table);

        assertEquals(0, house.getFurnitureCount());
        assertFalse(house.containsFurniture(table));
    }

    @Test
    void removeFurniture_throwsExceptionWhenFurnitureIsNull() {
        House house = new House("Home", new ArrayList<>());

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> house.removeFurniture(null));

        assertEquals("Furniture cannot be null.", ex.getMessage());
    }

    @Test
    void canAddFurniture_returnsFalseWhenFull() {
        House house = new House("Home", new ArrayList<>());

        for (int i = 1; i <= house.getMaxFurnitureCapacity(); i++) {
            house.addFurniture(makeFurniture("Furniture " + i));
        }

        assertFalse(house.canAddFurniture());
    }

    @Test
    void upgradeTo_copiesTierRatePriceAndFurniture() {
        ArrayList<Furniture> currentFurniture = new ArrayList<>();
        currentFurniture.add(makeFurniture("Old Bed"));

        House currentHouse = new House("Current", currentFurniture, 1000.0, 1.0, 1);

        ArrayList<Furniture> newFurniture = new ArrayList<>();
        Furniture sofa = makeFurniture("Sofa");
        Furniture lamp = makeFurniture("Lamp");
        newFurniture.add(sofa);
        newFurniture.add(lamp);

        House purchasedHouse = new House("Luxury", newFurniture, 8000.0, 3.5, 5);

        currentHouse.upgradeTo(purchasedHouse);

        assertEquals(5, currentHouse.getTier());
        assertEquals(3.5, currentHouse.getRate());
        assertEquals(8000.0, currentHouse.getPrice());
        assertEquals(2, currentHouse.getFurnitureCount());
        assertTrue(currentHouse.containsFurniture(sofa));
        assertTrue(currentHouse.containsFurniture(lamp));
    }

    @Test
    void upgradeTo_throwsExceptionWhenPurchasedHouseIsNull() {
        House house = new House("Home", new ArrayList<>());

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> house.upgradeTo(null));

        assertEquals("Purchased house cannot be null.", ex.getMessage());
    }

    @Test
    void getFurnitureViews_returnsUnmodifiableCopy() {
        House house = new House("Home", new ArrayList<>());
        house.addFurniture(makeFurniture("Desk"));

        List<Furniture> furnitureView = house.getFurnitureViews();

        assertThrows(UnsupportedOperationException.class,
                () -> furnitureView.add(makeFurniture("Extra")));
    }
}