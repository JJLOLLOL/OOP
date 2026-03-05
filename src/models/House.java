package models;

import java.util.ArrayList;

import models.activities.Activity;

public class House extends Location {
    
    public House(String LocationName, ArrayList<Activity> activities, ArrayList<NPCCharacter> npcs) {
        super(LocationName, activities, npcs);
        //TODO
    }

    public String HouseName;

    public String getHouseName() {
        return HouseName;
    }

    public void setHouseName(String HouseName) {
        this.HouseName = HouseName;
    }
}
