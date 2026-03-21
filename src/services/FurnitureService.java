package services;

import models.House;
import models.SimCharacter;
import models.actions.Furniture;

public class FurnitureService {

    public static final int MAX_FURNITURE_SIZE = 5;

    private FurnitureService() {

    }

    public static boolean purchaseFurniture(SimCharacter buyer, House house, Furniture furniture) {
        // Check if the buyer has enough money
        if (buyer.getMoney() < furniture.getPrice()) {
            return false; // Not enough money to purchase
        }

        // Check if the house has enough space for the furniture
        if (house.getFurnitures().size() >= MAX_FURNITURE_SIZE) {
            return false; // House is full, cannot add more furniture
        }
        //Deduct money and add furniture to the house
        buyer.setMoney(-furniture.getPrice());
        house.getFurnitures().add(furniture);

        return true; // Furniture purchased successfully
    }

    public static String getPurchaseMessage(SimCharacter buyer, House house, Furniture furniture, boolean success) {
        if (!success) {
            if (buyer.getMoney() < furniture.getPrice()) {
                return "Insufficient funds! Need $" + furniture.getPrice() + ", have: $" + buyer.getMoney();
            } else if (house.getFurnitures().size() >= MAX_FURNITURE_SIZE) {
                return "House is at maximum furniture capacity!";
            }
        }
        return buyer.getName() + " purchased " + furniture.getName() + " for $" + furniture.getPrice();
    }

    public static boolean removeFurniture(House house, Furniture furniture) {
        if (house.getFurnitures().contains(furniture)) {
            house.getFurnitures().remove(furniture);
            return true;
        }
        return false;
    }
}
