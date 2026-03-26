package services;

import models.actions.Furniture;
import models.character.SimCharacter;
import models.location.House;

public class FurnitureService {

    private FurnitureService() {

    }

    public static boolean purchaseFurniture(SimCharacter buyer, House house, Furniture furniture) {
        // Check if the buyer has enough money
        if (buyer.getMoney() < furniture.getPrice()) {
            return false; // Not enough money to purchase
        }

        // Check if the house has enough space for the furniture
        if (house.getFurnitureViews().size() >= house.getMaxFurnitureCapacity()) {
            return false; // House is full, cannot add more furniture
        }
        //Deduct money and add furniture to the house
        buyer.spendMoney(furniture.getPrice());
        house.getFurnitureViews().add(furniture);

        return true; // Furniture purchased successfully
    }

    public static String getPurchaseMessage(SimCharacter buyer, House house, Furniture furniture, boolean success) {
        if (!success) {
            if (buyer.getMoney() < furniture.getPrice()) {
                return "Insufficient funds! Need $" + furniture.getPrice() + ", have: $" + buyer.getMoney();
            } else if (house.getFurnitureViews().size() >= house.getMaxFurnitureCapacity()) {
                return "House is at maximum furniture capacity! (" + house.getMaxFurnitureCapacity() + " items)";
            }
        }
        return buyer.getName() + " purchased " + furniture.getName() + " for $" + furniture.getPrice();
    }

    public static boolean removeFurniture(House house, Furniture furniture) {
        if (house.getFurnitureViews().contains(furniture)) {
            house.getFurnitureViews().remove(furniture);
            return true;
        }
        return false;
    }

    /**
     * Sells a furniture item from the player's house.
     *
     * <p>
     * Validates that the furniture exists in the house, then removes it from the inventory
     * and refunds the player 50% of the original purchase price via
     *
     * @param seller the {@link SimCharacter} selling the furniture (receives refund)
     * @param house the {@link House} where the furniture is located
     * @param furniture the {@link Furniture} to sell
     * @return {@code true} if the sale was successful; {@code false} if furniture not found
     */
    public static boolean sellFurniture(SimCharacter seller, House house, Furniture furniture) {
        // Check if the house contains the furniture
        if (!house.getFurnitureViews().contains(furniture)) {
            return false; // Furniture not found in the house
        }

        // Remove furniture from house and refund player (50% of original price)
        house.getFurnitureViews().remove(furniture);
        double refundAmount = furniture.getPrice() * 0.5;
        seller.earnMoney(refundAmount);

        return true; // Furniture sold successfully
    }

    /**
     * Generates a user-friendly message describing the result of a furniture sale.
     *
     * <p>
     * On success, displays the seller's name, furniture name, and refund amount (50% of price).
     * On failure, displays an error message indicating the furniture was not found in the house.
     *
     * @param seller the {@link SimCharacter} who sold the furniture
     * @param house the {@link House} from which furniture was removed
     * @param furniture the {@link Furniture} that was sold
     * @param success whether the sale was successful
     * @return a formatted message suitable for display to the player
     */
    public static String getSellMessage(SimCharacter seller, House house, Furniture furniture, boolean success) {
        if (!success) {
            if (!house.getFurnitureViews().contains(furniture)) {
                return "Furniture not found in house!";
            }
        }
        double refundAmount = furniture.getPrice() * 0.5;
        return seller.getName() + " sold " + furniture.getName() + " for $" + String.format("%.2f", refundAmount);
    }
}
