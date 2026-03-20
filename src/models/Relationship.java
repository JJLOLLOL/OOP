package models;

import Types.RelationshipList;

public class Relationship {

    private static final int MIN = -100;
    private static final int MAX = 100;

    private int score;

    public Relationship() {
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public RelationshipList getStatus() {
        return RelationshipList.from(score);
    }

    public void changeScore(int amount) {
        score = Math.max(MIN, Math.min(MAX, score + amount));
    }
}
