package models;

import java.util.ArrayList;
import models.furnitureactions.Furniture;

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

    // Validation for furniture limit
    public void validateFurnitureLimit() {

        if (getFurnitures().size() > 3) {
            throw new IllegalStateException("A house can contain at most 3 furniture items.");
        }
    }
}