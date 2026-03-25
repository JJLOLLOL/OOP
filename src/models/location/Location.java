package models.location;

import java.util.ArrayList;
import java.util.List;
import models.actions.Furniture;
import models.character.NPCCharacter;

/**
 * Represents a distinct physical area in the game world.
 * A location contains a name, a list of interactable furniture items,
 * and a list of NPCs currently present there.
 */
public class Location {

    private String locationName;
    private ArrayList<Furniture> furnitures;
    private ArrayList<NPCCharacter> npcs;

    /**
     * Constructs a new {@code Location}.
     *
     * @param locationName the name of the location
     * @param furnitures   a list of initial {@link Furniture} items in this location
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

    /**
     * Retrieves the name of the location.
     *
     * @return the location name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Retrieves the list of furniture items present in the location.
     *
     * @return a list of {@link Furniture}
     */
    public List<Furniture> getFurnitures() {
        return furnitures;
    }

    /**
     * Retrieves the list of NPCs currently in the location.
     *
     * @return a list of {@link NPCCharacter}s
     */
    public List<NPCCharacter> getNpcs() {
        return npcs;
    }

    /**
     * Sets the list of NPCs currently in the location.
     *
     * @param npcs the new list of {@link NPCCharacter}s
     */
    public void setNpcs(ArrayList<NPCCharacter> npcs) {
        this.npcs = npcs;
    }

    /**
     * Adds an NPC character to this location.
     *
     * @param npc the {@link NPCCharacter} to add
     */
    public void addNpcCharacter(NPCCharacter npc) {
        npcs.add(npc);
    }

    /**
     * Removes an NPC character from this location.
     *
     * @param npc the {@link NPCCharacter} to remove
     */
    public void removeNpcCharacter(NPCCharacter npc) {
        npcs.remove(npc);
    }

    /**
     * Prints a list of all furniture available in this location to standard output.
     */
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