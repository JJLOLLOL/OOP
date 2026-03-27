package data;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import data.parser.FurnitureParser;
import data.parser.LocationParser;
import data.parser.NpcParser;
import data.parser.ShopParser;
import models.character.NPCCharacter;
import models.character.SimCharacter;
import models.furniture.Furniture;
import models.location.Location;

/**
 * Orchestrates the loading of all world data from text files by delegating
 * to specialized parser classes.
 */
public class WorldLoader {

    /**
     * Loads all world data from the data files.
     *
     * @return A {@link WorldData} object containing all loaded locations and NPCs.
     * @throws RuntimeException if data files cannot be read.
     */
    public WorldData loadWorldData() {
        try {
            // 1. Parse all furniture and their actions
            FurnitureParser furnitureParser = new FurnitureParser();
            Map<String, Furniture> furnitureMap = furnitureParser.parse("data/furniture.txt");

            // Set the global work action for the SimCharacter class
            setGlobalWorkAction(furnitureMap);

            // 2. Parse locations, using the furniture map
            LocationParser locationParser = new LocationParser();
            Map<String, Location> locations = locationParser.parse("data/locations.txt", furnitureMap);
            // 3. Parse NPCs, using the locations map
            NpcParser npcParser = new NpcParser();
            List<NPCCharacter> npcs = npcParser.parse("data/npcs.txt", locations);
            // 4. Parse shop inventory, using the furniture map
            ShopParser shopParser = new ShopParser();
            ShopInventory shopInventory = shopParser.parse("data/shop.txt", furnitureMap);

            return new WorldData(locations, npcs, shopInventory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load world data. Game cannot start.", e);
        }
    }

    /**
     * Extracts the global work action from the parsed furniture data and
     * stores it on {@link SimCharacter} for later work-shift calculations.
     */
    private void setGlobalWorkAction(Map<String, Furniture> furnitureMap) {
        Furniture workDesk = furnitureMap.get("WorkDesk");
        if (workDesk != null && workDesk.getAction("Work") != null) {
            SimCharacter.setWorkAction(workDesk.getAction("Work"));
        } else {
            // This will cause a crash later if work() is called, which is intended to highlight the missing data.
            System.err.println("CRITICAL: Could not find 'Work' action for 'WorkDesk' in furniture.txt. The 'work' command will fail.");
        }
    }
}
