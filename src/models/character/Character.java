package models.character;


import models.character.relationship.CharacterRelationship;
import models.location.Location;
import types.Gender;
import types.RelationshipType;

public abstract class Character {
    private final String name;
    private final int age;
    private final Gender gender;
    private Location currentLocation;
    private final CharacterRelationship relationships;

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

    public void initializeRelationshipWith(Character other) {
        relationships.initializeWith(other);
    }

    public void changeRelationshipWith(Character other, int delta) {
        relationships.changeRelationshipWith(other, delta);
    }

    public int getRelationshipScoreWith(Character other) {
        return relationships.getScoreWith(other);
    }

    public RelationshipType getRelationshipStatus(Character other) {
        return relationships.getStatusWith(other);
    }

    public Location getLocation() {
        return currentLocation;
    }

    public void setLocation(Location location) {
        currentLocation = location;
    }
}