package data;

import java.util.List;
import java.util.Map;
import models.character.NPCCharacter;
import models.location.Location;

/**
 * A container for all static world data loaded from external files.
 */
public class WorldData {
    private final Map<String, Location> locations;
    private final List<NPCCharacter> npcs;
    private final ShopInventory shopInventory;

    public WorldData(Map<String, Location> locations, List<NPCCharacter> npcs, ShopInventory shopInventory) {
        this.locations = locations;
        this.npcs = npcs;
        this.shopInventory = shopInventory;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public ShopInventory getShopInventory() {
        return shopInventory;
    }

    public List<NPCCharacter> getNpcs() {
        return npcs;
    }
}