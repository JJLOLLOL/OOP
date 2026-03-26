package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import models.furniture.Furniture;
import models.location.House;

class ShopInventoryTest {

    @Test
    void constructorAndGetters_shouldStoreProvidedLists() {
        List<House> houses = new ArrayList<>();
        List<Furniture> furniture = new ArrayList<>();

        ShopInventory inventory = new ShopInventory(houses, furniture);

        assertSame(houses, inventory.getAvailableHouses());
        assertSame(furniture, inventory.getAvailableFurniture());
    }

    @Test
    void getters_shouldReturnListsWithExpectedSizes() {
        List<House> houses = new ArrayList<>();
        List<Furniture> furniture = new ArrayList<>();

        houses.add(new House("Starter House", new ArrayList<>(), 1000.0, 10.0, 1));
        houses.add(new House("Family House", new ArrayList<>(), 5000.0, 20.0, 2));

        furniture.add(new Furniture("Chair", "Simple chair", 50.0));
        furniture.add(new Furniture("Desk", "Wooden desk", 150.0));

        ShopInventory inventory = new ShopInventory(houses, furniture);

        assertEquals(2, inventory.getAvailableHouses().size());
        assertEquals(2, inventory.getAvailableFurniture().size());
    }
}