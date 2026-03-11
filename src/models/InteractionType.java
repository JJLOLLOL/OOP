package models;

public enum InteractionType {

  TALK(5),
  COMPLIMENT(10),
  ARGUE(-10),
  INSULT(-15);

  private int value;

  InteractionType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}