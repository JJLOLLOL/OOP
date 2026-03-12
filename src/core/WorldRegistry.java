package core;

import models.Location;
import models.House;
import models.furnitureactions.Furniture;
import models.furnitureactions.FurnitureFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldRegistry {
    private static WorldRegistry instance;
    private final Map<String, Location> locationsMap = new HashMap<>();

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
        Furniture cheapBed = FurnitureFactory.createCheapMattress();
        Furniture hotplate = FurnitureFactory.createSingleHotplate();

        House home = new House("Home", new ArrayList<>(List.of(cheapBed)));
        registerLocation(home);

        Location restaurant = new Location("Restaurant", new ArrayList<>(List.of(hotplate)));
        registerLocation(restaurant);
    }

    private void connect(Location a, Location b) {
        // For now, assume all registered locations are reachable from anywhere.
        // Todo: implement an adjacency list to define travel paths.
    }

    private void registerLocation(Location location) {
        locationsMap.put(location.getLocationName(), location);
    }

    public Location getLocation(String name) {
        return locationsMap.get(name);
    }

    public Collection<Location> getAllLocations() {
        return Collections.unmodifiableCollection(locationsMap.values());
    }
}
