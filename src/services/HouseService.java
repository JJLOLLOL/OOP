package services;

import models.House;
import models.SimCharacter;

/**
 * Provides services related to purchasing and upgrading houses for Sims.
 */
public class HouseService {

    private HouseService() {

    }

    /**
     * Purchases a house for a buyer by upgrading their current home in-place.
     * This mutates the player's existing "Home" house to match the purchased house's properties
     * and furniture, ensuring the "Home" location remains the player's actual home and that
     * NPCs' schedules (which reference "Home" by name) remain valid.
     *
     * <p>
     * This approach avoids creating duplicate home locations in the registry and ensures
     * that changing location always returns the player to their actual home.
     *
     * @param buyer the {@link SimCharacter} purchasing the house
     * @param purchasedHouse the {@link House} being purchased
     * @return {@code true} if purchase was successful; {@code false} otherwise
     */
    public static boolean purchaseHouse(SimCharacter buyer, House purchasedHouse) {
        if (purchasedHouse.isOwned()) {
            return false; // House is already owned
        }
        if (buyer.getMoney() < purchasedHouse.getHousePrice()) {
            return false; // Not enough money to purchase
        }

        // Deduct money and upgrade the player's current home in-place
        buyer.setMoney(-purchasedHouse.getHousePrice());
        buyer.getCurrentHouse().upgradeToHouse(purchasedHouse);

        return true;
    }

    /**
     * Generates a user-friendly message describing the result of a house purchase attempt.
     *
     * @param buyer   the {@link SimCharacter} attempting the purchase
     * @param house   the {@link House} being purchased
     * @param success {@code true} if the purchase succeeded; {@code false} otherwise
     * @return a formatted message detailing the outcome
     */
    public static String getPurchaseMessage(SimCharacter buyer, House house, boolean success) {
        if (!success) {
            if (house.isOwned()) {
                return "Sorry, this house is already owned.";
            } else {
                return "Insufficient funds! You need $" + house.getHousePrice() + " to purchase this house.";
            }
        }
        return buyer.getName() + " purchased " + house.getLocationName() + " for $" + house.getHousePrice() + "!";
    }

    /**
     * Upgrades a Sim's house to the next tier by mutating their current home in-place.
     *
     * <p>
     * Upgrades the player's existing "Home" house to match the next tier house's properties,
     * deducts the upgrade cost from the owner's money, and updates their location to reflect
     * the upgraded home. The "Home" location name and NPC schedules remain valid.
     *
     * @param owner the {@link SimCharacter} purchasing the upgrade
     * @param nextHouse the next tier {@link House} to upgrade to
     * @return {@code true} if upgrade was successful; {@code false} if validation failed
     */
    public static boolean upgradeHouse(SimCharacter owner, House nextHouse) {
        House currentHouse = owner.getCurrentHouse();

        if (currentHouse == null) {
            return false; // Owner has no current house
        }
        if (nextHouse.getHouseTier() != currentHouse.getHouseTier() + 1) {
            return false; // Can only upgrade to the next tier
        }

        double upgradeCost = nextHouse.getHousePrice() - currentHouse.getHousePrice();

        if (owner.getMoney() < upgradeCost) {
            return false; // Not enough money for upgrade
        }

        // Process upgrade: mutate current house in-place to next tier
        owner.setMoney(-upgradeCost);
        currentHouse.upgradeToHouse(nextHouse);
        owner.setLocation(currentHouse);

        return true;
    }

}
