package models.character;


import models.character.relationship.CharacterRelationship;
import models.location.Location;
import types.Gender;
import types.RelationshipType;

/**
 * Base type for all characters in the world, including playable sims and NPCs.
 */
public abstract class Character {
    private final String name;
    private final int age;
    private final Gender gender;
    private Location currentLocation;
    private final CharacterRelationship relationships;

    /**
     * Creates a character with identity, starting location, and empty
     * relationship state.
     *
     * @param name the character's name
     * @param age the character's age
     * @param gender the character's gender
     * @param defaultLocation the character's starting location
     */
    protected Character(String name, int age, Gender gender, Location defaultLocation) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        if (defaultLocation == null) {
            throw new IllegalArgumentException("Default location cannot be null.");
        }
    
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.currentLocation = defaultLocation;
        this.relationships = new CharacterRelationship(this);
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }
    public String getName() {
        return name;
    }

    public CharacterRelationship getRelationships() {
        return relationships;
    }

    /**
     * Creates a shared relationship entry between this character and another
     * character.
     *
     * @param other the other character
     */
    public void initializeRelationshipWith(Character other) {
        relationships.initializeWith(other);
    }

    /**
     * Applies a relationship delta between this character and another
     * character.
     *
     * @param other the relationship target
     * @param delta the score delta to apply
     */
    public void changeRelationshipWith(Character other, int delta) {
        relationships.changeRelationshipWith(other, delta);
    }

    /**
     * Returns the numeric relationship score with another character.
     *
     * @param other the relationship target
     * @return the current relationship score
     */
    public int getRelationshipScoreWith(Character other) {
        return relationships.getScoreWith(other);
    }

    /**
     * Returns the derived relationship tier with another character.
     *
     * @param other the relationship target
     * @return the current relationship tier
     */
    public RelationshipType getRelationshipStatus(Character other) {
        return relationships.getStatusWith(other);
    }

    public Location getLocation() {
        return currentLocation;
    }

    /**
     * Moves the character to a new location.
     *
     * @param location the new location
     */
    public void setLocation(Location location) {
        currentLocation = location;
    }
}
