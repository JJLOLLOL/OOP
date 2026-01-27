package src.models;
import models.Activity;
import models.NPCCharacter;
import java.util.ArrayList;

public class Location {
    ArrayList<Activity> activities = new ArrayList<Activity>();
    public String LocationName;
    ArrayList<NPCCharacter> npcs = new ArrayList<NPCCharacter>();

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