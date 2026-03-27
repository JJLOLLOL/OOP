package types;

public enum RelationshipType {

    ENEMY("Enemy", -100, -50),
    DISLIKED("Disliked", -49, -25),
    ACQUAINTANCE("Acquaintance", -24, 24),
    FRIENDLY("Friendly", 25, 49),
    FRIEND("Friend", 50, 69),
    BEST_FRIEND("Best Friend", 70, 100);

    public final String label;
    private final int min;
    private final int max;

    RelationshipType(String label, int min, int max) {
        this.label = label;
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the tier for the given score.
     */
    public static RelationshipType from(int score) {
        for (RelationshipType tier : values()) {
            if (score >= tier.min && score <= tier.max) {
                return tier;
            }
        }
        return ACQUAINTANCE;
    }
}
