package Types;

import java.util.ArrayList;
import java.util.List;
import models.House;
import models.actions.Furniture;
import models.actions.FurnitureFactory;

public class ShopInventory {

    // Available houses for purchase (Tier 2+)
    public static List<House> getAvailableHouses() {
        List<House> houses = new ArrayList<>();

        houses.add(new House("Cozy Apartment", new ArrayList<>(), 5000, 1.2, 2));
        houses.add(new House("Modern House", new ArrayList<>(), 10000, 1.5, 3));
        houses.add(new House("Luxury Cottage", new ArrayList<>(), 25000, 2.0, 4));
        houses.add(new House("Mansion", new ArrayList<>(), 50000, 3.0, 5));

        return houses;
    }

    // Available furniture for purchase (Tier 1+)
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
