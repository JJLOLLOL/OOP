package models;

import java.util.ArrayList;
import models.furnitureactions.Furniture;

public class Location {
    // public ArrayList<Activity> activities;
    public ArrayList<Furniture> furnitures;
    public String LocationName;
    public ArrayList<NPCCharacter> npcs;

    public Location(String LocationName, ArrayList<Furniture> furnitures, ArrayList<NPCCharacter> npcs) {
        this.LocationName = LocationName;
        // this.activities = activities;
        this.furnitures = furnitures;
        this.npcs = npcs;
    }
   
    public void listFurnitures() {
        System.out.println("Furnitures in " + LocationName + ":");
        for (Furniture furniture : furnitures) {
            System.out.println("- " + furniture.getName());
        }
    }

    // public ArrayList<Activity> getActivities() {
    //     return activities;
    // }

    // public void setActivities(ArrayList<Activity> activities) {
    //     this.activities = activities;
    // }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String LocationName) {
        this.LocationName = LocationName;
    }

    public ArrayList<NPCCharacter> getNpcs() {
        return npcs;
    }

    public void setNpcs(ArrayList<NPCCharacter> npcs) {
        this.npcs = npcs;
    }


}