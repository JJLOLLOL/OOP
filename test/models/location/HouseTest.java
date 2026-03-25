package models.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import models.actions.Furniture;
import org.junit.jupiter.api.Test;

class HouseTest {

    @Test
    void defaultConstructorUsesStarterValues() {
        House house = new House("Starter Home", new ArrayList<>());

        assertEquals(1, house.getTier());
        assertEquals(1.0, house.getRate());
        assertEquals(0.0, house.getPrice());
        assertEquals(6, house.getMaxFurnitureCapacity());
    }

    @Test
    void addFurnitureRejectsNullAndRespectsCapacity() {
        House house = new House("Starter Home", new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> house.addFurniture(null));

        for (int i = 0; i < house.getMaxFurnitureCapacity(); i++) {
            house.addFurniture(new Furniture("Item " + i, "Decor", 10));
        }

        assertFalse(house.canAddFurniture());
        assertThrows(IllegalStateException.class,
                () -> house.addFurniture(new Furniture("Extra", "Decor", 10)));
    }

    @Test
    void removeFurnitureReducesFurnitureCount() {
        House house = new House("Starter Home", new ArrayList<>());
        Furniture chair = new Furniture("Chair", "Wooden chair", 25);

        house.addFurniture(chair);
        house.removeFurniture(chair);

        assertEquals(0, house.getFurnitureCount());
        assertTrue(house.canAddFurniture());
    }

    @Test
    void upgradeCopiesHouseStatsAndFurniture() {
        ArrayList<Furniture> upgradedFurniture = new ArrayList<>();
        upgradedFurniture.add(new Furniture("Bed", "King bed", 500));
        upgradedFurniture.add(new Furniture("Desk", "Study desk", 200));
        House currentHouse = new House("Current", new ArrayList<>());
        House upgradedHouse = new House("Villa", upgradedFurniture, 10000, 2.5, 3);

        currentHouse.upgradeTo(upgradedHouse);

        assertEquals(3, currentHouse.getTier());
        assertEquals(2.5, currentHouse.getRate());
        assertEquals(10000, currentHouse.getPrice());
        assertEquals(2, currentHouse.getFurnitureCount());
        assertEquals(8, currentHouse.getMaxFurnitureCapacity());
    }

    @Test
    void fullConstructorUsesProvidedValuesAndSupportsHigherTierFallbackCapacity() {
        House mansion = new House("Mansion", new ArrayList<>(), 50000, 4.5, 7);

        assertEquals(7, mansion.getTier());
        assertEquals(4.5, mansion.getRate());
        assertEquals(50000, mansion.getPrice());
        assertEquals(12, mansion.getMaxFurnitureCapacity());
    }

    @Test
    void upgradeRejectsNullHouse() {
        House house = new House("Starter Home", new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> house.upgradeTo(null));
    }

    @Test
    void removeFurnitureRejectsNull() {
        House house = new House("Starter Home", new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> house.removeFurniture(null));
    }
}
