package models.character.relationship;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import models.character.Character;
import models.character.Relationship;
import types.RelationshipType;

/**
 * Stores the relationship graph for one owning character and keeps symmetric
 * relationship objects shared between both participants.
 */
public class CharacterRelationship {
    private final Character owner;
    private final Map<Character, Relationship> relationships = new HashMap<>();

    /**
     * Creates a relationship container for the supplied owner.
     *
     * @param owner the character that owns this relationship collection
     */
    public CharacterRelationship(Character owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null.");
        }
        this.owner = owner;
    }

    /**
     * Initializes a shared relationship object between the owner and another
     * character.
     *
     * @param other the other character
     */
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

    /**
     * Applies a relationship score delta, lazily initializing the relationship
     * first when required.
     *
     * @param target the relationship target
     * @param delta the score delta to apply
     */
    public void changeRelationshipWith(Character target, int delta) {
        validateTarget(target);
        if (!relationships.containsKey(target)) {
            initializeWith(target);
        }
        relationships.get(target).adjust(delta);
    }

    /**
     * Returns the numeric relationship score with the supplied target.
     *
     * @param target the relationship target
     * @return the current score, or {@code 0} when no relationship exists yet
     */
    public int getScoreWith(Character target) {
        validateTarget(target);
        Relationship relationship = relationships.get(target);
        return relationship == null ? 0 : relationship.getScore();
    }

    /**
     * Returns the derived relationship tier with the supplied target.
     *
     * @param target the relationship target
     * @return the relationship tier, or {@link RelationshipType#ACQUAINTANCE}
     * when no relationship exists yet
     */
    public RelationshipType getStatusWith(Character target) {
        validateTarget(target);
        Relationship relationship = relationships.get(target);
        return relationship == null
                ? RelationshipType.ACQUAINTANCE
                : relationship.getStatus();
    }

    /**
     * Returns an unmodifiable view of all relationship entries for the owner.
     *
     * @return the relationship map
     */
    public Map<Character, Relationship> getRelationshipViews() {
        return Collections.unmodifiableMap(relationships);
    }

    /**
     * Validates that a relationship target is non-null before use.
     */
    private void validateTarget(Character target) {
        if (target == null) {
            throw new IllegalArgumentException("Relationship target cannot be null.");
        }
    }
}
