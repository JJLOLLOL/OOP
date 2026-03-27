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

    /**
     * Creates a neutral relationship with score {@code 0}.
     */
    public Relationship() {
        this.score = DEFAULT_SCORE;
    }

    public int getScore() {
        return score;
    }

    public RelationshipType getStatus() {
        return RelationshipType.from(score);
    }

    /**
     * Adjusts the relationship score while clamping it to the allowed range.
     *
     * @param delta the score delta to apply
     */
    public void adjust(int delta) {
        if (delta == 0) {
            return;
        }
        score = clamp(score + delta);
    }

    /**
     * Clamps the relationship score to the supported min/max bounds.
     */
    private int clamp(int value) {
        return Math.max(MIN_SCORE, Math.min(MAX_SCORE, value));
    }
}
