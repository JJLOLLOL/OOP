package Types;

import java.util.ArrayList;
import java.util.List;

import models.actions.Furniture;
import models.actions.FurnitureFactory;
import models.location.House;

public class ShopInventory {

    // Available houses for purchase (Tier 2+)
    public static List<House> getAvailableHouses() {
        List<House> houses = new ArrayList<>();

        // Tier 2 - Cozy Apartment (capacity 7): Basic furnishings for starting upgrade
        houses.add(new House("Cozy Apartment", new ArrayList<>(List.of(
                FurnitureFactory.createSingleBed(),
                FurnitureFactory.createOldStove(),
                FurnitureFactory.createNormalShower(),
                FurnitureFactory.createToilet(),
                FurnitureFactory.createModernLCDTV()
        )), 1500, 1.2, 2));

        // Tier 3 - Modern House (capacity 8): Better quality furnishings
        houses.add(new House("Modern House", new ArrayList<>(List.of(
                FurnitureFactory.createDoubleBed(),
                FurnitureFactory.createModernStove(),
                FurnitureFactory.createNormalShower(),
                FurnitureFactory.createToilet(),
                FurnitureFactory.createOLEDTV(),
                FurnitureFactory.createBookshelf(),
                FurnitureFactory.createComputerDesk(),
                FurnitureFactory.createTreadmill()
        )), 5000, 1.5, 3));

        // Tier 4 - Luxury Cottage (capacity 9): Premium furnishings
        houses.add(new House("Luxury Cottage", new ArrayList<>(List.of(
                FurnitureFactory.createQueenBed(),
                FurnitureFactory.createGourmetStove(),
                FurnitureFactory.createLuxuryBathtub(),
                FurnitureFactory.createToilet(),
                FurnitureFactory.createOLEDTV(),
                FurnitureFactory.createBookshelf(),
                FurnitureFactory.createComputerDesk(),
                FurnitureFactory.createTreadmill(),
                FurnitureFactory.createDumbbells()
        )), 7500, 2.0, 4));

        // Tier 5 - Mansion (capacity 10+): Luxury furnishings
        houses.add(new House("Mansion", new ArrayList<>(List.of(
                FurnitureFactory.createKingBed(),
                FurnitureFactory.createGourmetStove(),
                FurnitureFactory.createLuxuryBathtub(),
                FurnitureFactory.createToilet(),
                FurnitureFactory.createOLEDTV(),
                FurnitureFactory.createBookshelf(),
                FurnitureFactory.createComputerDesk(),
                FurnitureFactory.createTreadmill(),
                FurnitureFactory.createDumbbells(),
                FurnitureFactory.createJukeBox()
        )), 10000, 3.0, 5));

        return houses;
    }

    // Available furniture for purchase
    public static List<Furniture> getAvailableFurniture() {
        List<Furniture> furniture = new ArrayList<>();

        furniture.add(FurnitureFactory.createCheapMattress());
        furniture.add(FurnitureFactory.createSingleBed());
        furniture.add(FurnitureFactory.createDoubleBed());
        furniture.add(FurnitureFactory.createQueenBed());
        furniture.add(FurnitureFactory.createKingBed());

        furniture.add(FurnitureFactory.createSingleHotplate());
        furniture.add(FurnitureFactory.createOldStove());
        furniture.add(FurnitureFactory.createModernStove());
        furniture.add(FurnitureFactory.createGourmetStove());

        furniture.add(FurnitureFactory.createOldShower());
        furniture.add(FurnitureFactory.createNormalShower());
        furniture.add(FurnitureFactory.createLuxuryBathtub());

        furniture.add(FurnitureFactory.createToilet());

        furniture.add(FurnitureFactory.createOldCRTTV());
        furniture.add(FurnitureFactory.createModernLCDTV());
        furniture.add(FurnitureFactory.createOLEDTV());

        furniture.add(FurnitureFactory.createTreadmill());
        furniture.add(FurnitureFactory.createDumbbells());
        furniture.add(FurnitureFactory.createEspressoMachine());
        furniture.add(FurnitureFactory.createJukeBox());
        furniture.add(FurnitureFactory.createBookshelf());
        furniture.add(FurnitureFactory.createComputerDesk());
        furniture.add(FurnitureFactory.createBicycle());

        return furniture;
    }
}
