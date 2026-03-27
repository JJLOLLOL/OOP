package services;

import models.character.SimCharacter;
import models.location.House;

/**
 * Legacy helper methods for house purchasing and upgrading flows.
 */
public class HouseService {

    /**
     * Prevents instantiation of this utility class.
     */
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
        // if (purchasedHouse.isOwned()) {
            // return false; // House is already owned
        //
        if (buyer.getMoney() < purchasedHouse.getPrice()) {
            return false; // Not enough money to purchase
        }

        // Deduct money and upgrade the player's current home in-place
        buyer.spendMoney(purchasedHouse.getPrice());
        buyer.getCurrentHouse().upgradeTo(purchasedHouse);

        return true;
    }

    /**
     * Builds the legacy success message shown after a house purchase.
     *
     * @param buyer the sim attempting the purchase
     * @param house the house being purchased
     * @param success unused legacy flag kept for compatibility
     * @return the message that should be shown to the player
     */
    public static String getPurchaseMessage(SimCharacter buyer, House house, boolean success) {
        // if (!success) {
            // if (house.isOwned()) {
                // return "Sorry, this house is already owned.";
            // } else {
                // return "Insufficient funds! You need $" + house.getPrice() + " to purchase this house.";
            // }
        // }
        return buyer.getName() + " purchased " + house.getLocationName() + " for $" + house.getPrice() + "!";
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
        if (nextHouse.getTier() != currentHouse.getTier() + 1) {
            return false; // Can only upgrade to the next tier
        }

        double upgradeCost = nextHouse.getPrice() - currentHouse.getPrice();

        if (!owner.canAfford(upgradeCost)) {
            return false; // Not enough money for upgrade
        }

        // Process upgrade: mutate current house in-place to next tier
        owner.spendMoney(upgradeCost);
        currentHouse.upgradeTo(nextHouse);
        owner.setLocation(currentHouse);

        return true;
    }

}
