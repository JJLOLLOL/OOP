package core;

import models.House;
import models.Location;
import models.furnitureactions.Furniture;
import models.furnitureactions.FurnitureFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldBuilder {

    protected Map<String, Location> buildWorld() {
         Map<String, Location> locationsMap = new HashMap<>();
        
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
        locationsMap.put(home.getLocationName(), home);

        Location restaurant = new Location("Restaurant", new ArrayList<>(List.of(restaurantTable, toilet)));
        locationsMap.put(restaurant.getLocationName(), restaurant);

        Location gym = new Location("Gym", new ArrayList<>(List.of(treadmill, dumbbells, vendingMachine)));
        locationsMap.put(gym.getLocationName(), gym);

        Location park = new Location("Park", new ArrayList<>(List.of(parkPath, parkLake, bicycle, toilet, picnicTable)));
        locationsMap.put(park.getLocationName(), park);

        Location cafe = new Location("Cafe", new ArrayList<>(List.of(cafeTable, espressoMachine, jukeBox, toilet)));
        locationsMap.put(cafe.getLocationName(), cafe);

        Location library = new Location("Library", new ArrayList<>(List.of(bookshelf, computerDesk, toilet)));
        locationsMap.put(library.getLocationName(), library);

        Location club = new Location("Club", new ArrayList<>(List.of(bar, danceFloor, toilet)));
        locationsMap.put(club.getLocationName(), club);

        return locationsMap;
    }
}
