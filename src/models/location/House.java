package models.location;

import java.util.ArrayList;

import models.furniture.Furniture;

/**
 * Specialised {@link Location} that can be owned, upgraded, and furnished by a
 * sim.
 */
public class House extends Location {

    private int houseTier;
    private double houseRate;
    private double housePrice;

    /**
     * Creates a house definition with explicit tier and pricing metadata.
     *
     * @param locationName the display name of the house
     * @param furnitures the furniture currently in the house
     * @param housePrice the purchase price of the house
     * @param houseRate an additional house multiplier value used by gameplay
     * @param houseTier the house tier
     */
    public House(String locationName, ArrayList<Furniture> furnitures, double housePrice, double houseRate, int houseTier) {
        super(locationName, furnitures);
        this.housePrice = housePrice;
        this.houseRate = houseRate;
        this.houseTier = houseTier;
    }

    /**
     * Creates the default starter house state used for the player's home.
     *
     * @param locationName the display name of the house
     * @param furnitures the furniture currently in the house
     */
    public House(String locationName, ArrayList<Furniture> furnitures) {
        super(locationName, furnitures);
        this.housePrice = 0;
        this.houseRate = 1;
        this.houseTier = 1;
    }

    public int getTier() {
        return houseTier;
    }

    public double getRate() {
        return houseRate;
    }

    public double getPrice() {
        return housePrice;
    }

    /**
     * Returns the maximum number of furniture items allowed in this house based
     * on its tier.
     *
     * @return the furniture capacity
     */
    public int getMaxFurnitureCapacity() {
        return Math.max(6, this.houseTier + 5);
    }

    /**
     * Mutates this house to match the tier, price, and furniture of another
     * house definition.
     *
     * @param purchasedHouse the house definition being upgraded to
     */
    public void upgradeTo(House purchasedHouse) {
        if (purchasedHouse == null) {
            throw new IllegalArgumentException("Purchased house cannot be null.");
        }

        this.houseTier = purchasedHouse.houseTier;
        this.houseRate = purchasedHouse.houseRate;
        this.housePrice = purchasedHouse.housePrice;

        replaceFurnitureInternal(purchasedHouse.getFurnitureViews());
    }

    /**
     * Returns whether another furniture item can be added without exceeding
     * capacity.
     *
     * @return {@code true} when furniture can still be added
     */
    public boolean canAddFurniture() {
        return getFurnitureCount() < getMaxFurnitureCapacity();
    }

    /**
     * Adds one furniture item to the house.
     *
     * @param furniture the furniture to add
     */
    public void addFurniture(Furniture furniture) {
        if (furniture == null) {
            throw new IllegalArgumentException("Furniture cannot be null.");
        }
        if (!canAddFurniture()) {
            throw new IllegalStateException("Furniture capacity reached.");
        }
        addFurnitureInternal(furniture);
    }

    /**
     * Removes one furniture item from the house.
     *
     * @param furniture the furniture to remove
     */
    public void removeFurniture(Furniture furniture) {
        if (furniture == null) {
            throw new IllegalArgumentException("Furniture cannot be null.");
        }
        removeFurnitureInternal(furniture);
    }

    public int getFurnitureCount() {
        return getFurnitureViews().size();
    }
}
