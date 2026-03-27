package types;

/**
 * Social interactions available between characters, including their
 * relationship effects and canned reactions.
 */
public enum InteractionType {

    TALK("Talk", 5, " responds positively to the conversation."),
    COMPLIMENT("Compliment", 10, " smiles and thanks you warmly."),
    ARGUE("Argue", -10, " responds negatively and walks away."),
    INSULT("Insult", -15, " looks hurt and storms off angrily.");

    private final String label;
    private final int effect;
    private final String reaction;

    InteractionType(String label, int effect, String reaction) {
        this.label = label;
        this.effect = effect;
        this.reaction = reaction;
    }

    public String getLabel() {
        return label;
    }

    public int getEffect() {
        return effect;
    }

    public String getReaction() {
        return reaction;
    }
}
