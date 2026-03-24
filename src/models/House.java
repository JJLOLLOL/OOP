package models;

import java.util.ArrayList;
import models.actions.Furniture;

/**
 * Represents a specialized residential {@link Location} that a Sim can own,
 * upgrade, and furnish. Houses have tiers, prices, and furniture capacity limits.
 */
public class House extends Location {

    private int houseTier;
    private double houseRate;
    private double housePrice;
    private boolean isOwned;

    /**
     * Constructs a purchasable, higher-tier house.
     *
     * @param locationName the name of the house/location
     * @param furnitures   the initial list of furniture
     * @param housePrice   the purchase price of the house
     * @param houseRate    the utility rate or multiplier associated with the house
     * @param houseTier    the tier level of the house (determines capacity)
     */
    public House(String locationName, ArrayList<Furniture> furnitures,
            double housePrice, double houseRate, int houseTier) {

        super(locationName, furnitures);

        this.housePrice = housePrice;
        this.houseRate = houseRate;
        this.houseTier = houseTier;
        this.isOwned = false;
    }

    /**
     * Constructs a default Tier 1 house that is already owned.
     *
     * @param locationName the name of the house/location
     * @param furnitures   the initial list of furniture
     */
    public House(String locationName, ArrayList<Furniture> furnitures) {

        super(locationName, furnitures);

        this.housePrice = 0;
        this.houseRate = 1;
        this.houseTier = 1;
        this.isOwned = true;
    }

    /**
     * Retrieves the tier level of the house.
     *
     * @return the house tier
     */
    public int getHouseTier() {
        return houseTier;
    }

    /**
     * Retrieves the rate or multiplier of the house.
     *
     * @return the house rate
     */
    public double getHouseRate() {
        return houseRate;
    }

    /**
     * Retrieves the purchase price of the house.
     *
     * @return the house price
     */
    public double getHousePrice() {
        return housePrice;
    }

    /**
     * Checks if the house is currently owned by a player.
     *
     * @return {@code true} if owned, {@code false} otherwise
     */
    public boolean isOwned() {
        return isOwned;
    }

    /**
     * Sets the ownership status of the house.
     *
     * @param owned {@code true} to mark as owned, {@code false} otherwise
     */
    public void setOwned(boolean owned) {
        this.isOwned = owned;
    }

    /**
     * Calculates and returns the maximum number of furniture items the house can hold
     * based on its current tier.
     *
     * @return the maximum furniture capacity
     */
    public int getMaxFurnitureCapacity() {
        return switch(this.houseTier) {
            case 1 -> 6;
            case 2 -> 7;
            case 3 -> 8;
            case 4 -> 9;
            case 5 -> 10;
            default -> Math.max(6, this.houseTier + 5);
        };
    }

    /**
     * Upgrades the current house to the specified next-tier house.
     * Transfers ownership status.
     *
     * @param nextTierHouse the {@link House} object representing the next tier
     * @throws IllegalArgumentException if the next house is null or not exactly one tier higher
     */
    public void upgradeHouse(House nextTierHouse) {

        if (nextTierHouse == null) {
            throw new IllegalArgumentException("Next tier house cannot be null.");
        }

        if (nextTierHouse.houseTier != this.houseTier + 1) {
            throw new IllegalArgumentException("Upgrade must be to the next house tier.");
        }

        // Transfer ownership
        this.isOwned = false;
        nextTierHouse.isOwned = true;
    }

    /**
     * Mutates this house in-place to match the properties of the purchased house.
     * Replaces the furniture, tier, rate, and price while keeping the location name as "Home".
     * This ensures the "Home" location always represents the player's current house and  
     * that NPCs' schedules remain valid.
     *
     * <p>
     * Call this when the player purchases a house to avoid creating duplicate home locations
     * in the registry.
     *
     * @param purchasedHouse the {@link House} that was just purchased
     */
    public void upgradeToHouse(House purchasedHouse) {
        if (purchasedHouse == null) {
            throw new IllegalArgumentException("Purchased house cannot be null.");
        }

        // Copy the properties from the purchased house into this Home house
        this.houseTier = purchasedHouse.houseTier;
        this.houseRate = purchasedHouse.houseRate;
        this.housePrice = purchasedHouse.housePrice;
        this.isOwned = true;

        // Replace the furniture list with the purchased house's furniture
        this.getFurnitures().clear();
        this.getFurnitures().addAll(purchasedHouse.getFurnitures());
    }

    /**
     * Validates that the house's current furniture count does not exceed its capacity.
     *
     * @throws IllegalStateException if the furniture limit is exceeded
     */
    public void validateFurnitureLimit() {

        if (getFurnitures().size() > getMaxFurnitureCapacity()) {
            throw new IllegalStateException("A house can contain at most " + getMaxFurnitureCapacity() + " furniture items.");
        }
    }

    /**
     * Adds a furniture item to the house.
     *
     * @param furniture the {@link Furniture} to add
     * @return {@code true} if the furniture was added; {@code false} if at
     * capacity
     */
    public boolean addFurniture(Furniture furniture) {
        if (furnitures.size() >= getMaxFurnitureCapacity()) {
            return false;
        }
        return furnitures.add(furniture);
    }

    /**
     * Removes a furniture item from the house.
     *
     * @param furniture the {@link Furniture} to remove
     * @return {@code true} if the furniture was removed; {@code false} if not
     * found
     */
    public boolean removeFurniture(Furniture furniture) {
        return furnitures.remove(furniture);
    }

    /**
     * Gets the current number of furniture items in the house.
     *
     * @return the furniture count
     */
    public int getFurnitureCount() {
        return furnitures.size();
    }

    /**
     * Checks if the house contains a specific furniture item.
     *
     * @param furniture the {@link Furniture} to check for
     * @return {@code true} if the house contains this furniture
     */
    public boolean hasFurniture(Furniture furniture) {
        return furnitures.contains(furniture);
    }
}
