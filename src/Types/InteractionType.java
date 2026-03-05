package Types;

public enum InteractionType {

  TALK("Talk", 5),
  COMPLIMENT("Compliment", 10),
  ARGUE("Argue", -10),
  INSULT("Insult", -15);

  private String label;
  private int effect;

  InteractionType(String label, int effect) {
    this.label = label;
    this.effect = effect;
  }

  public int getValue() {
    return effect;
  }

  public String getLabel() {
    return label;
  }
}
