package core;

import models.Location;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class WorldRegistry {
    private static WorldRegistry instance;
    private Map<String, Location> locationsMap;

    private WorldRegistry() {
        buildWorld();
    }

    public static WorldRegistry getInstance() {
        if (instance == null) {
            instance = new WorldRegistry();
        }
        return instance;
    }

    private void buildWorld() {
        WorldBuilder builder = new WorldBuilder();
        this.locationsMap = builder.buildWorld();
    }


    public Location getLocation(String name) {
        return locationsMap.get(name);
    }

    public Collection<Location> getAllLocations() {
        return Collections.unmodifiableCollection(locationsMap.values());
    }
}
