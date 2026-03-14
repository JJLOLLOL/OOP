package models;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class NPCCharacter extends Character {
    private TreeMap<Integer, Location> schedule;
    private String description;

    // Constructor 1: Game auto-generates schedule from locations
    public NPCCharacter(String name, int age, String gender, ArrayList<Location> locations) {
        this(name, age, gender, "", locations);
    }

    // Constructor 1 with description
    public NPCCharacter(String name, int age, String gender, String description, ArrayList<Location> locations) {
        super(name, age, gender, locations.get(0)); // first location = default
        this.description = description;
        this.schedule = generateSchedule(locations);
    }

    // Constructor 2: Player provides schedule directly
    public NPCCharacter(String name, int age, String gender, TreeMap<Integer, Location> schedule) {
        this(name, age, gender, "", schedule);
    }

    // Constructor 2 with description
    public NPCCharacter(String name, int age, String gender, String description, TreeMap<Integer, Location> schedule) {
        super(name, age, gender, schedule.firstEntry().getValue()); // first location = default
        this.description = description;
        this.schedule = schedule;
    }

    // Helper method to generate schedule from locations list
    private TreeMap<Integer, Location> generateSchedule(ArrayList<Location> locations) {
        TreeMap<Integer, Location> schedule = new TreeMap<>();
        
        // Auto-generate schedule: distribute locations across the day
        if (locations.isEmpty()) {
            return schedule;
        }
        
        // Default schedule generation logic
        int[] defaultTimes = {9, 12, 18, 20}; // Morning, Lunch, Evening, Night
        for (int i = 0; i < locations.size() && i < defaultTimes.length; i++) {
            schedule.put(defaultTimes[i], locations.get(i));
        }
        
        return schedule;
    }

    public void displaySchedule() {
        System.out.println("Schedule for " + getName() + ":");
        for (Map.Entry<Integer, Location> entry : schedule.entrySet()) {
            System.out.println("  " + entry.getKey() + ":00 - " + entry.getValue().getLocationName());
        }
    }

    public TreeMap<Integer, Location> getSchedule() {
        return schedule;
    }

    public void setSchedule(TreeMap<Integer, Location> schedule) {
        this.schedule = schedule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
