package services;

import models.House;
import models.SimCharacter;
import models.actions.Furniture;

public class FurnitureService {

    private FurnitureService() {

    }

    public static boolean purchaseFurniture(SimCharacter buyer, House house, Furniture furniture) {
        // Check if the buyer has enough money
        if (buyer.getMoney() < furniture.getPrice()) {
            return false; // Not enough money to purchase
        }

        // Check if the house has enough space for the furniture
        if (house.getFurnitures().size() >= house.getMaxFurnitureCapacity()) {
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
            } else if (house.getFurnitures().size() >= house.getMaxFurnitureCapacity()) {
                return "House is at maximum furniture capacity! (" + house.getMaxFurnitureCapacity() + " items)";
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

    public static boolean sellFurniture(SimCharacter seller, House house, Furniture furniture) {
        // Check if the house contains the furniture
        if (!house.getFurnitures().contains(furniture)) {
            return false; // Furniture not found in the house
        }

        // Remove furniture from house and refund player (50% of original price)
        house.getFurnitures().remove(furniture);
        double refundAmount = furniture.getPrice() * 0.5;
        seller.setMoney(refundAmount);

        return true; // Furniture sold successfully
    }

    public static String getSellMessage(SimCharacter seller, House house, Furniture furniture, boolean success) {
        if (!success) {
            if (!house.getFurnitures().contains(furniture)) {
                return "Furniture not found in house!";
            }
        }
        double refundAmount = furniture.getPrice() * 0.5;
        return seller.getName() + " sold " + furniture.getName() + " for $" + String.format("%.2f", refundAmount);
    }
}
