package models;

public class Relationship {
  private int score;

  public Relationship(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }

  public void changeScore(int value) {
    score += value;
    if (score > 100)
      score = 100;
    if (score < -100)
      score = -100;
  }
}