package models.location;

import java.util.ArrayList;
import java.util.List;

import models.character.NPCCharacter;
import models.furniture.Furniture;

/**
 * Represents a place in the world containing furniture and any NPCs currently
 * present there.
 */
public class Location {

    private String locationName;
    private ArrayList<Furniture> furnitures;
    private ArrayList<NPCCharacter> npcs;

    /**
     * Creates a location with the supplied furniture list.
     *
     * @param locationName the display name of the location
     * @param furnitures the initial furniture in the location
     */
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

    /**
     * Returns whether the supplied furniture item is currently present in this
     * location.
     *
     * @param furniture the furniture to check for
     * @return {@code true} when the furniture is present
     */
    public boolean containsFurniture(Furniture furniture) {
        return furnitures.contains(furniture);
    }

    public List<Furniture> getFurnitureViews() {
        return List.copyOf(furnitures);
    }

    public List<NPCCharacter> getNpcViews() {
        return List.copyOf(npcs);
    }

    /**
     * Registers an NPC as currently present in this location.
     *
     * @param npc the NPC to add
     */
    public void addNpcCharacter(NPCCharacter npc) {
        npcs.add(npc);
    }

    /**
     * Removes an NPC from this location's presence list.
     *
     * @param npc the NPC to remove
     */
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
