package src.models;
import java.util.ArrayList;
import models.Activity;
import models.NPCCharacter;

public class Location {
    public ArrayList<Activity> activities;
    public String LocationName;
    public ArrayList<NPCCharacter> npcs;

    public Location(String LocationName, ArrayList<Activity> activities, ArrayList<NPCCharacter> npcs) {
        this.LocationName = LocationName;
        this.activities = activities;
        this.npcs = npcs;
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


}