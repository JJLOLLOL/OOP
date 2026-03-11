package models;
import java.util.ArrayList;
import models.Activity;
import models.NPCCharacter;

public class Location {
    public ArrayList<Activity> activities;
    public String LocationName;
    public ArrayList<NPCCharacter> npcs;
    //location is first object to be created in game
    public Location(String LocationName, ArrayList<Activity> activities) {// no npc in constructor, will set when npcs created
        this.LocationName = LocationName;
        this.activities = activities;

    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }


    
    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
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