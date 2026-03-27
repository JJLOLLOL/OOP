package models.character;

import types.RelationshipType;

/**
 * Represents one relationship bond between two characters.
 * Score is clamped between MIN and MAX.
 */
public class Relationship {

    private static final int MIN_SCORE = -100;
    private static final int MAX_SCORE = 100;
    private static final int DEFAULT_SCORE = 0;

    private int score;

    public Relationship() {
        this.score = DEFAULT_SCORE;
    }

    public int getScore() {
        return score;
    }

    public RelationshipType getStatus() {
        return RelationshipType.from(score);
    }

    public void adjust(int delta) {
        if (delta == 0) {
            return;
        }
        score = clamp(score + delta);
    }

    private int clamp(int value) {
        return Math.max(MIN_SCORE, Math.min(MAX_SCORE, value));
    }
}