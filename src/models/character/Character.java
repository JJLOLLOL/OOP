package models.character;


import java.util.Map;

import Types.RelationshipList;
import models.character.relationship.CharacterRelationship;
import models.location.Location;

public abstract class Character {
    private final String name;
    private final int age;
    private final String gender;
    private Location currentLocation;
    private final CharacterRelationship relationships;

    protected Character(String name, int age, String gender, Location defaultLocation) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.currentLocation = defaultLocation;
        this.relationships = new CharacterRelationship(this);
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
    public String getName() {
        return name;
    }

    public Map<Character, Relationship> getRelationshipViews() {
        return relationships.getRelationshipViews();
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

    public RelationshipList getRelationshipStatus(Character other) {
        return relationships.getStatusWith(other);
    }

    public Location getLocation() {
        return currentLocation;
    }

    public void setLocation(Location location) {
        currentLocation = location;
    }
}