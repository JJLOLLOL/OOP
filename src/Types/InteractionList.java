package Types;

/**
 * Represents the different types of social interactions available between characters.
 * Each interaction has an associated effect on the relationship score and a corresponding reaction text.
 */
public enum InteractionList {

    TALK("Talk", 5, " responds positively to the conversation."),
    COMPLIMENT("Compliment", 10, " smiles and thanks you warmly."),
    ARGUE("Argue", -10, " responds negatively and walks away."),
    INSULT("Insult", -15, " looks hurt and storms off angrily.");

    private final String label;
    private final int effect;
    private final String reaction;

    /**
     * Constructs a new InteractionList enum constant.
     *
     * @param label    the display label of the interaction
     * @param effect   the numerical change applied to the relationship score
     * @param reaction the text displayed describing the target character's reaction
     */
    InteractionList(String label, int effect, String reaction) {
        this.label = label;
        this.effect = effect;
        this.reaction = reaction;
    }

    /**
     * Retrieves the display label of the interaction.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retrieves the numerical effect of the interaction on the relationship score.
     *
     * @return the effect value
     */
    public int getEffect() {
        return effect;
    }

    /**
     * Retrieves the reaction text associated with the interaction.
     *
     * @return the reaction string
     */
    public String getReaction() {
        return reaction;
    }
}
