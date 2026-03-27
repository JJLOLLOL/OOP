package data.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import data.ShopInventory;
import models.furniture.Furniture;
import models.location.House;

class ShopParserTest {

    @Test
    void parse_shouldBuildInventoryAndFilterUnknownFurniture() throws IOException {
        Furniture desk = new Furniture("Test Desk", "Coding desk", 200.0);
        Furniture arcade = new Furniture("Arcade Machine", "Fun machine", 500.0);

        Map<String, Furniture> furnitureMap = new HashMap<>();
        furnitureMap.put("TestDesk", desk);
        furnitureMap.put("ArcadeMachine", arcade);

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;

        ShopInventory inventory;
        try {
            System.setErr(new PrintStream(errContent));
            inventory = new ShopParser().parse("test/fixtures/data/shop-fixture.txt", furnitureMap);
        } finally {
            System.setErr(originalErr);
        }

        assertEquals(2, inventory.getAvailableFurniture().size());
        assertSame(desk, inventory.getAvailableFurniture().get(0));
        assertSame(arcade, inventory.getAvailableFurniture().get(1));

        assertEquals(2, inventory.getAvailableHouses().size());

        House condo = inventory.getAvailableHouses().get(0);
        assertEquals("Condo", condo.getLocationName());
        assertEquals(1500.0, condo.getPrice(), 0.001);
        assertEquals(1.2, condo.getRate(), 0.001);
        assertEquals(2, condo.getTier());
        assertEquals(1, condo.getFurnitureCount());
        assertTrue(condo.containsFurniture(desk));

        House villa = inventory.getAvailableHouses().get(1);
        assertEquals("Villa", villa.getLocationName());
        assertEquals(4000.0, villa.getPrice(), 0.001);
        assertEquals(2.1, villa.getRate(), 0.001);
        assertEquals(4, villa.getTier());
        assertEquals(1, villa.getFurnitureCount());
        assertTrue(villa.containsFurniture(arcade));

        assertTrue(errContent.toString().contains("Shop inventory lists unknown furniture: MissingFurniture"));
    }

    @Test
    void parse_shouldReturnEmptyInventoryWhenNoRelevantBlocksExist() throws IOException {
        Path file = Path.of("src/test/fixtures/data/shop-empty-fixture.txt");
        Files.createDirectories(file.getParent());
        Files.writeString(file, """
                # no shop blocks here
                [OTHER_BLOCK]
                VALUE: ignored
                """);

        try {
            ShopInventory inventory = new ShopParser().parse("test/fixtures/data/shop-empty-fixture.txt", Map.of());

            assertTrue(inventory.getAvailableFurniture().isEmpty());
            assertTrue(inventory.getAvailableHouses().isEmpty());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void parse_shouldHandleMissingNamesAndHouseWithoutFurniture() throws IOException {
        Furniture desk = new Furniture("Desk", "Simple desk", 100.0);

        Map<String, Furniture> furnitureMap = new HashMap<>();
        furnitureMap.put("Desk", desk);

        Path file = Path.of("src/test/fixtures/data/shop-missing-names-fixture.txt");
        Files.createDirectories(file.getParent());
        Files.writeString(file, """
                [AVAILABLE_FURNITURE]
                PRICE: ignored

                [HOUSE_FOR_SALE]
                NAME: Starter Home
                PRICE: 800
                RATE: 1.0
                TIER: 1
                """);

        try {
            ShopInventory inventory = new ShopParser().parse("test/fixtures/data/shop-missing-names-fixture.txt", furnitureMap);

            assertTrue(inventory.getAvailableFurniture().isEmpty());
            assertEquals(1, inventory.getAvailableHouses().size());

            House house = inventory.getAvailableHouses().get(0);
            assertEquals("Starter Home", house.getLocationName());
            assertEquals(800.0, house.getPrice(), 0.001);
            assertEquals(1.0, house.getRate(), 0.001);
            assertEquals(1, house.getTier());
            assertEquals(0, house.getFurnitureCount());
        } finally {
            Files.deleteIfExists(file);
        }
    }
}