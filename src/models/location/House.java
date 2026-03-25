package models.location;

import java.util.ArrayList;
import models.actions.Furniture;

public class House extends Location {

    private int houseTier;
    private double houseRate;
    private double housePrice;

    public House(String locationName, ArrayList<Furniture> furnitures, double housePrice, double houseRate, int houseTier) {
        super(locationName, furnitures);
        this.housePrice = housePrice;
        this.houseRate = houseRate;
        this.houseTier = houseTier;
    }

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

    public void upgradeTo(House purchasedHouse) {
        if (purchasedHouse == null) {
            throw new IllegalArgumentException("Purchased house cannot be null.");
        }

        this.houseTier = purchasedHouse.houseTier;
        this.houseRate = purchasedHouse.houseRate;
        this.housePrice = purchasedHouse.housePrice;

        replaceFurnitureInternal(purchasedHouse.getFurnitureViews());
    }
    public boolean canAddFurniture() {
        return getFurnitureCount() < getMaxFurnitureCapacity();
    }
    
    public void addFurniture(Furniture furniture) {
        if (furniture == null) {
            throw new IllegalArgumentException("Furniture cannot be null.");
        }
        if (!canAddFurniture()) {
            throw new IllegalStateException("Furniture capacity reached.");
        }
        addFurnitureInternal(furniture);
    }
    
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
