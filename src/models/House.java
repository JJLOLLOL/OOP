package models;

import java.util.ArrayList;
import models.actions.Furniture;

public class House extends Location {
    //add 3 max furnitures only.

    private int houseTier;
    private double houseRate;
    private double housePrice;
    private boolean isOwned;

    // Constructor for purchasable / higher tier houses
    public House(String locationName, ArrayList<Furniture> furnitures,
            double housePrice, double houseRate, int houseTier) {

        super(locationName, furnitures);

        this.housePrice = housePrice;
        this.houseRate = houseRate;
        this.houseTier = houseTier;
        this.isOwned = false;
    }

    // Constructor for default Tier 1 house (owned)
    public House(String locationName, ArrayList<Furniture> furnitures) {

        super(locationName, furnitures);

        this.housePrice = 0;
        this.houseRate = 1;
        this.houseTier = 1;
        this.isOwned = true;
    }

    public int getHouseTier() {
        return houseTier;
    }

    public double getHouseRate() {
        return houseRate;
    }

    public double getHousePrice() {
        return housePrice;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public void setOwned(boolean owned) {
        this.isOwned = owned;
    }

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

    // Upgrade current house to the next tier
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

    // Validation for furniture limit
    public void validateFurnitureLimit() {

        if (getFurnitures().size() > getMaxFurnitureCapacity()) {
            throw new IllegalStateException("A house can contain at most " + getMaxFurnitureCapacity() + " furniture items.");
        }
    }
}
