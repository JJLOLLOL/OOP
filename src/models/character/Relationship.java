package models.character;

import Types.RelationshipList;

/**
 * Tracks the relationship score between characters.
 * The score is bounded between a defined minimum and maximum
 * and corresponds to various social statuses.
 */
public class Relationship {

    private static final int MIN = -100;
    private static final int MAX = 100;

    private int score;

    /**
     * Constructs a new {@code Relationship} starting with a neutral score of 0.
     */
    public Relationship() {
        this.score = 0;
    }

    /**
     * Retrieves the current relationship score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Evaluates the current numeric score and returns the corresponding relationship status.
     *
     * @return the {@link RelationshipList} status enum
     */
    public RelationshipList getStatus() {
        return RelationshipList.from(score);
    }

    /**
     * Changes the relationship score by a given amount, ensuring it remains within bounds.
     *
     * @param amount the positive or negative amount to add to the score
     */
    public void changeScore(int amount) {
        score = Math.max(MIN, Math.min(MAX, score + amount));
    }
}
