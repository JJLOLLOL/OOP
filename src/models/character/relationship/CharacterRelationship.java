package models.character.relationship;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import models.character.Character;
import models.character.Relationship;
import types.RelationshipType;

public class CharacterRelationship {
    private final Character owner;
    private final Map<Character, Relationship> relationships = new HashMap<>();

    public CharacterRelationship(Character owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null.");
        }
        this.owner = owner;
    }

    public void initializeWith(Character other) {
        validateTarget(other);
        if (other == owner) {
            throw new IllegalArgumentException("Character cannot relate to itself.");
        }

        if (relationships.containsKey(other)) {
            return;
        }

        Relationship shared = new Relationship();
        relationships.put(other, shared);
        other.getRelationships().attachRelationship(owner, shared);
    }

    void attachRelationship(Character other, Relationship relationship) {
        relationships.put(other, relationship);
    }

    public void changeRelationshipWith(Character target, int delta) {
        validateTarget(target);
        if (!relationships.containsKey(target)) {
            initializeWith(target);
        }
        relationships.get(target).adjust(delta);
    }

    public int getScoreWith(Character target) {
        validateTarget(target);
        Relationship relationship = relationships.get(target);
        return relationship == null ? 0 : relationship.getScore();
    }

    public RelationshipType getStatusWith(Character target) {
        validateTarget(target);
        Relationship relationship = relationships.get(target);
        return relationship == null
                ? RelationshipType.ACQUAINTANCE
                : relationship.getStatus();
    }

    public Map<Character, Relationship> getRelationshipViews() {
        return Collections.unmodifiableMap(relationships);
    }

    private void validateTarget(Character target) {
        if (target == null) {
            throw new IllegalArgumentException("Relationship target cannot be null.");
        }
    }
}