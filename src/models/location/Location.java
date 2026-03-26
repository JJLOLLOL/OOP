package models.location;

import java.util.ArrayList;
import java.util.List;
import models.actions.Furniture;
import models.character.NPCCharacter;
import ui.ActionResult;

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

    public boolean containsFurniture(Furniture furniture) {
        return furnitures.contains(furniture);
    }

    public List<Furniture> getFurnitureViews() {
        return List.copyOf(furnitures);
    }

    public List<NPCCharacter> getNpcViews() {
        return List.copyOf(npcs);
    }

    public void addNpcCharacter(NPCCharacter npc) {
        npcs.add(npc);
    }

    public void removeNpcCharacter(NPCCharacter npc) {
        npcs.remove(npc);
    }
    
    protected void addFurnitureInternal(Furniture furniture) {
        if (furniture == null) {
            throw new IllegalArgumentException("Furniture cannot be null.");
        }
        furnitures.add(furniture);
    }
    
    protected boolean removeFurnitureInternal(Furniture furniture) {
        if (furniture == null) {
            throw new IllegalArgumentException("Furniture cannot be null.");
        }
        furnitures.remove(furniture);
        return true;
    }
    
    protected void replaceFurnitureInternal(List<Furniture> newFurniture) {
        if (newFurniture == null) {
            throw new IllegalArgumentException("Furniture list cannot be null.");
        }
        furnitures.clear();
        furnitures.addAll(newFurniture);
    }
}
