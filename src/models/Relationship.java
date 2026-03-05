package models;

import Types.InteractionType;

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

  public void applyInteraction(InteractionType type, String fromName, String toName) {
    int value = type.getValue();
    changeScore(value);
    System.out.print(fromName + " " + type.getLabel() + " " + toName + ". Relationship ");
    if (value > 0)
      System.out.println("improved to " + score);
    else if (value < 0)
      System.out.println("worsened to " + score);
    else
      System.out.println("remain unchanged");
  }
  
  public String getStatus() {
    if (score > 50)
      return "Friend";
    else if (score > 0)
      return "Acquaintance";
    else if (score > -25)
      return "Stranger";
    else if (score > -100)
      return "Enemy";
    else
      return "Nemesis";
  }
}