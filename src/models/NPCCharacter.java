package models;

import java.util.TreeMap;

public class NPCCharacter extends Character {

    private final TreeMap<Integer, Location> schedule;
    private final String description;

    public NPCCharacter(String name, int age, String gender, String description, TreeMap<Integer, Location> schedule) {
        super(name, age, gender, schedule.firstEntry().getValue());
        this.description = description;
        this.schedule = schedule;
    }

    public TreeMap<Integer, Location> getSchedule() {
        return schedule;
    }

    public String getDescription() {
        return description;
    }
}
