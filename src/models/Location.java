package models;

import java.util.ArrayList;
import java.util.List;
import models.furnitureactions.Furniture;

public class Location {

    private String locationName;
    private ArrayList<Furniture> furnitures;
    private ArrayList<NPCCharacter> npcs;

    public Location(String locationName, ArrayList<Furniture> furnitures) {

        this.locationName = locationName;

        if (furnitures == null) {
            this.furnitures = new ArrayList<>();
        } else {
            this.furnitures = furnitures;
        }

        this.npcs = new ArrayList<>();
    }

    public String getLocationName() {
        return locationName;
    }

    public List<Furniture> getFurnitures() {
        return furnitures;
    }

    public List<NPCCharacter> getNpcs() {
        return npcs;
    }

    public void setNpcs(ArrayList<NPCCharacter> npcs) {
        this.npcs = npcs;
    }

    public void addNpcCharacter(NPCCharacter npc) {
        npcs.add(npc);
    }

    public void removeNpcCharacter(NPCCharacter npc) {
        npcs.remove(npc);
    }

    public void listFurnitures() {
        System.out.println("Furnitures in " + locationName + ":");

        if (furnitures.isEmpty()) {
            System.out.println("No furniture available.");
            return;
        }

        for (Furniture furniture : furnitures) {
            System.out.println("- " + furniture.getName());
        }
    }
}