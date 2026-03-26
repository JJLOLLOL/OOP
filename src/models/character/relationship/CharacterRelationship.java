package models.character.relationship;

import Types.RelationshipList;
import models.character.Relationship;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CharacterRelationship {

    private final Map<models.character.Character, Relationship> relationships = new HashMap<>();

    public void initializeWith(models.character.Character other) {
        validateTarget(other);
        relationships.putIfAbsent(other, new Relationship());
    }

    public void changeRelationshipWith(models.character.Character target, int delta) {
        validateTarget(target);
        relationships.computeIfAbsent(target, key -> new Relationship()).adjust(delta);
    }

    public int getScoreWith(models.character.Character target) {
        validateTarget(target);
        Relationship relationship = relationships.get(target);
        return relationship == null ? 0 : relationship.getScore();
    }

    public RelationshipList getStatusWith(models.character.Character target) {
        validateTarget(target);
        Relationship relationship = relationships.get(target);
        return relationship == null
                ? RelationshipList.ACQUAINTANCE
                : relationship.getStatus();
    }

    public boolean hasRelationshipWith(models.character.Character target) {
        validateTarget(target);
        return relationships.containsKey(target);
    }

    public Map<models.character.Character, Relationship> getRelationshipViews() {
        return Collections.unmodifiableMap(relationships);
    }

    private void validateTarget(models.character.Character target) {
        if (target == null) {
            throw new IllegalArgumentException("Relationship target cannot be null.");
        }
    }
}