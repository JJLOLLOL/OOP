package Types;

public enum InteractionType {

  TALK("Talk", 5, " responds positively to the conversation."),
  COMPLIMENT("Compliment", 10, " smiles and thanks you warmly."),
  ARGUE("Argue", -10, " responds negatively and walks away."),
  INSULT("Insult", -15, " looks hurt and storms off angrily.");

  private String label;
  private int effect;
  private String reaction;

  InteractionType(String label, int effect, String reaction) {
    this.label = label;
    this.effect = effect;
    this.reaction = reaction;
  }

  public int getValue() {
    return effect;
  }

  public String getLabel() {
    return label;
  }

  public String getReaction() {
    return reaction;
  }
}
