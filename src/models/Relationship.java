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

  public String applyInteraction(InteractionType type, String fromName, String toName) {
    String interaction = fromName + " " + type.getLabel() + " " + toName + "\n" + toName + type.getReaction() + "\nRelationship with " + toName + " ";
    int value = type.getValue();
    changeScore(value);
    if (value > 0)
      interaction += " improved to " + score;
    else if (value < 0)
      interaction += " worsened to " + score;
    else
      interaction += "remain unchanged";
    return interaction;
  }
  
  public String getStatus() {
    if (score <= -50)
      return "Enemy";
    else if (score <= -25)
      return "Disliked";
    else if (score < 25)
      return "Acquaintance";
    else if (score < 50)
      return "Friendly";
    else if (score < 70)
      return "Friend";
    else
      return "Best Friend";
  }
}