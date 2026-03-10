package models;

import java.util.ArrayList;
import models.furnitureactions.Furniture;

public class House extends Location {

    int houseTier;
    double houseRate;
    double housePrice;
    boolean isOwned;

    public House(String LocationName, ArrayList<Furniture> furnitures, ArrayList<NPCCharacter> npcs, double housePrice,
            double houseRate, int houseTier) {
        super(LocationName, furnitures, npcs);
        this.housePrice = housePrice;
        this.houseRate = houseRate;
        this.houseTier = houseTier;
        this.isOwned = false;
    }
    
    // for upgraded houses
    public House(String LocationName, ArrayList<Furniture> furnitures, ArrayList<NPCCharacter> npcs) {
        super(LocationName, furnitures, npcs);
        this.housePrice = 0;
        this.houseRate = 1;
        this.houseTier = 1;
        this.isOwned = true;

        // implement constructor in a constructor
    }// only for new house

    public int getHouseTier() {
        return houseTier;
    }

    public double getHouseRate() {
        return houseRate;
    }

    public double getHousePrice() {
        return housePrice;
    }

    public boolean isIsOwned() {
        return isOwned;
    }

    public void setIsOwned(boolean isOwned) {
        this.isOwned = isOwned;
    }
    // IS OWNED can only be set to True

}
