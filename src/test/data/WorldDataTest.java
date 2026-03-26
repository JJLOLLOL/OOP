package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.character.NPCCharacter;
import models.location.Location;

class WorldDataTest {

    @Test
    void constructorAndGetters_shouldStoreProvidedValues() {
        Map<String, Location> locations = new HashMap<>();
        List<NPCCharacter> npcs = new ArrayList<>();
        ShopInventory shopInventory = new ShopInventory(new ArrayList<>(), new ArrayList<>());

        WorldData worldData = new WorldData(locations, npcs, shopInventory);

        assertSame(locations, worldData.getLocations());
        assertSame(npcs, worldData.getNpcs());
        assertSame(shopInventory, worldData.getShopInventory());
    }

    @Test
    void getters_shouldReturnExpectedCollectionSizes() {
        Map<String, Location> locations = new HashMap<>();
        locations.put("Park", new Location("Park", new ArrayList<>()));
        locations.put("Mall", new Location("Mall", new ArrayList<>()));

        List<NPCCharacter> npcs = new ArrayList<>();
        ShopInventory shopInventory = new ShopInventory(new ArrayList<>(), new ArrayList<>());

        WorldData worldData = new WorldData(locations, npcs, shopInventory);

        assertEquals(2, worldData.getLocations().size());
        assertEquals(0, worldData.getNpcs().size());
    }
}