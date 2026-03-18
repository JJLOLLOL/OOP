package core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import models.Location;
import models.NPCCharacter;

/**
 * Holds the static world data (locations + NPCs). Built once at startup via
 * WorldBuilder.
 */
public class WorldRegistry {

    private final Map<String, Location> locationsMap;
    private final List<NPCCharacter> npcList;

    public WorldRegistry() {
        WorldBuilder builder = new WorldBuilder();
        this.locationsMap = builder.buildWorld();
        this.npcList = builder.buildNPCs(locationsMap);
    }

    public Location getLocation(String name) {
        return locationsMap.get(name);
    }

    public Collection<Location> getAllLocations() {
        return Collections.unmodifiableCollection(locationsMap.values());
    }

    public List<NPCCharacter> getAllNPCs() {
        return Collections.unmodifiableList(npcList);
    }
}
