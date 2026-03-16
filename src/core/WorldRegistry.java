package core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import models.Location;
import models.NPCCharacter;

public class WorldRegistry {
    private static WorldRegistry instance;
    private Map<String, Location> locationsMap;
    private List<NPCCharacter> npcList;

    private WorldRegistry() {
        build();
    }

    public static WorldRegistry getInstance() {
        if (instance == null) {
            instance = new WorldRegistry();
        }
        return instance;
    }

    private void build() {
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
        return npcList;
    }
}
