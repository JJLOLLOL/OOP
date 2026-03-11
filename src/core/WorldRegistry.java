package core;

import models.Location;

import java.util.HashMap;
import java.util.Map;

public class WorldRegistry {
    private static WorldRegistry instance;
    private Map<String, Location> locations = new HashMap<>();

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

    }

    private void connect(Location a, Location b) {

    }

    private void register() {

    }

}
