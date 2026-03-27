package models.character;

import java.util.TreeMap;

import models.location.Location;
import types.Gender;

/**
 * Represents a Non-Playable Character (NPC) in the game world.
 * NPCs possess a daily schedule that dictates their location at given times
 * and a description that can be seen by the player.
 */
public class NPCCharacter extends Character {

    private final TreeMap<Integer, Location> schedule;
    private final String description;

    /**
     * Constructs a new {@code NPCCharacter}.
     * The NPC's starting location is derived from the first entry in their schedule.
     *
     * @param name        the name of the NPC
     * @param age         the age of the NPC
     * @param gender      the gender of the NPC
     * @param description a short description of the NPC
     * @param schedule    a map representing the NPC's schedule (Time in HHMM format mapped to a Location)
     */
    public NPCCharacter(String name, int age, Gender gender, String description, TreeMap<Integer, Location> schedule) {
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
