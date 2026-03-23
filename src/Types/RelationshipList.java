package Types;

/**
 * Represents the tier status of a relationship between two characters.
 * Determined by the numerical relationship score.
 */
public enum RelationshipList {

    ENEMY("Enemy", -100, -50),
    DISLIKED("Disliked", -49, -25),
    ACQUAINTANCE("Acquaintance", -24, 24),
    FRIENDLY("Friendly", 25, 49),
    FRIEND("Friend", 50, 69),
    BEST_FRIEND("Best Friend", 70, 100);

    /**
     * The display label of the relationship tier.
     */
    public final String label;
    private final int min;
    private final int max;

    /**
     * Constructs a new RelationshipList enum constant.
     *
     * @param label the display label of the relationship tier
     * @param min   the minimum score required for this tier
     * @param max   the maximum score allowed for this tier
     */
    RelationshipList(String label, int min, int max) {
        this.label = label;
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the tier for the given score.
     *
     * @param score the numerical relationship score
     * @return the appropriate {@link RelationshipList} tier, defaulting to {@link #ACQUAINTANCE} if not found
     */
    public static RelationshipList from(int score) {
        for (RelationshipList tier : values()) {
            if (score >= tier.min && score <= tier.max) {
                return tier;
            }
        }
        return ACQUAINTANCE;
    }
}
