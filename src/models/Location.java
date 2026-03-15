package models;

import java.util.ArrayList;
import java.util.List;
import models.furnitureactions.Furniture;

public class Location {
    // public ArrayList<Activity> activities;
    public ArrayList<Furniture> furnitures;
    public String LocationName;
    public ArrayList<NPCCharacter> npcs;

    public Location(String LocationName, ArrayList<Furniture> furnitures) {
        this.LocationName = LocationName;
        // this.activities = activities;
        this.furnitures = furnitures;
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
    public List<Furniture> getFurniture() {
        return furnitures;
    }
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

    public ArrayList<NPCCharacter> addNpcCharacters(ArrayList<NPCCharacter> npcs, NPCCharacter npc) {
        npcs.add(npc);
        return npcs;
    }

    public ArrayList<NPCCharacter> removeNpcCharacters(ArrayList<NPCCharacter> npcs, NPCCharacter npc) {
        npcs.remove(npc);
        return npcs;
    }

}