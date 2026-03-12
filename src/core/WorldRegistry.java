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
        Furniture cheapMattress = FurnitureFactory.createCheapMattress();
        Furniture singleBed = FurnitureFactory.createSingleBed();
        Furniture doubleBed = FurnitureFactory.createDoubleBed();
        Furniture queenBed = FurnitureFactory.createQueenBed();
        Furniture kingBed = FurnitureFactory.createKingBed();
        Furniture hotplate = FurnitureFactory.createSingleHotplate();
        Furniture oldStove = FurnitureFactory.createOldStove();
        Furniture modernStove = FurnitureFactory.createModernStove();
        Furniture gourmetStove = FurnitureFactory.createGourmetStove();
        Furniture oldShower = FurnitureFactory.createOldShower();
        Furniture normalShower = FurnitureFactory.createNormalShower();
        Furniture luxuryBathtub = FurnitureFactory.createLuxuryBathtub();
        Furniture toilet = FurnitureFactory.createToilet();
        Furniture oldCRTTV = FurnitureFactory.createOldCRTTV();
        Furniture modernLCDTV = FurnitureFactory.createModernLCDTV();
        Furniture OLEDTV = FurnitureFactory.createOLEDTV();
        Furniture restaurantTable = FurnitureFactory.createRestaurantTable();
        Furniture treadmill = FurnitureFactory.createTreadmill();
        Furniture dumbbells = FurnitureFactory.createDumbbells();
        Furniture vendingMachine = FurnitureFactory.createVendingMachine();
        Furniture parkPath = FurnitureFactory.createParkPath();
        Furniture parkLake = FurnitureFactory.createParkLake();
        Furniture bicycle = FurnitureFactory.createBicycle();
        Furniture picnicTable = FurnitureFactory.createPicnicTable();
        Furniture cafeTable = FurnitureFactory.createCafeTable();
        Furniture espressoMachine = FurnitureFactory.createEspressoMachine();
        Furniture jukeBox = FurnitureFactory.createJukeBox();
        Furniture bookshelf = FurnitureFactory.createBookshelf();
        Furniture computerDesk = FurnitureFactory.createComputerDesk();
        Furniture bar = FurnitureFactory.createBar();
        Furniture danceFloor = FurnitureFactory.createDanceFloor();


        House home = new House("Home", new ArrayList<>(List.of(cheapMattress, oldStove, oldShower, toilet, oldCRTTV)));
        registerLocation(home);

        Location restaurant = new Location("Restaurant", new ArrayList<>(List.of(restaurantTable, toilet)));
        registerLocation(restaurant);

        Location gym = new Location("Gym", new ArrayList<>(List.of(treadmill, dumbbells, vendingMachine)));
        registerLocation(gym);

        Location park = new Location("Park", new ArrayList<>(List.of(parkPath, parkLake, bicycle, toilet, picnicTable)));
        registerLocation(park);

        Location cafe = new Location("Cafe", new ArrayList<>(List.of(cafeTable, espressoMachine, jukeBox, toilet)));
        registerLocation(cafe);

        Location library = new Location("Library", new ArrayList<>(List.of(bookshelf, computerDesk, toilet)));
        registerLocation(library);

        Location club = new Location("Club", new ArrayList<>(List.of(bar, danceFloor, toilet)));
        registerLocation(club);
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
