package data.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.furniture.Furniture;
import models.location.House;
import models.location.Location;

class LocationParserTest {

    @Test
    void parse_shouldCreateLocationsAndFilterUnknownFurniture() throws IOException {
        Furniture desk = new Furniture("Test Desk", "Coding desk", 200.0);
        Furniture arcade = new Furniture("Arcade Machine", "Fun machine", 500.0);
        Map<String, Furniture> furnitureMap = new HashMap<>();
        furnitureMap.put("TestDesk", desk);
        furnitureMap.put("ArcadeMachine", arcade);

        Map<String, Location> locations = new LocationParser().parse(
                "test/fixtures/data/locations-fixture.txt",
                furnitureMap);

        Location starterHome = locations.get("Starter Home");
        Location lounge = locations.get("Lounge");

        assertEquals(2, locations.size());
        assertNotNull(starterHome);
        assertNotNull(lounge);
        assertTrue(starterHome instanceof House);
        assertFalse(lounge instanceof House);
        assertTrue(starterHome.containsFurniture(desk));
        assertFalse(starterHome.containsFurniture(arcade));
        assertEquals(1, starterHome.getFurnitureViews().size());
        assertTrue(lounge.containsFurniture(arcade));
    }
}
