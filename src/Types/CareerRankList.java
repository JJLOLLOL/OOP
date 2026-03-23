package Types;

/**
 * Represents the progression ranks within a career.
 * Each rank provides a title and a salary multiplier.
 */
public enum CareerRankList {
    INTERN("Intern", 0.5),
    JUNIOR_EMPLOYEE("Junior Employee", 1.0),
    EMPLOYEE("Employee", 1.25),
    SENIOR_EMPLOYEE("Senior Employee", 1.7),
    MANAGER("Manager", 2.2),
    DIRECTOR("Director", 2.8),
    EXECUTIVE("Executive", 3.5);

    private final String title;
    private final double salaryMultiplier;

    /**
     * Constructs a new CareerRankList enum constant.
     *
     * @param title            the display title of the rank
     * @param salaryMultiplier the multiplier applied to the base salary of the career
     */
    CareerRankList(String title, double salaryMultiplier) {
        this.title = title;
        this.salaryMultiplier = salaryMultiplier;
    }

    /**
     * Retrieves the display title of the rank.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the salary multiplier for the rank.
     *
     * @return the salary multiplier
     */
    public double getSalaryMultiplier() {
        return salaryMultiplier;
    }

    /**
     * Returns the rank enum for a 1-based rank index.
     *
     * @param rank the 1-based index of the desired rank
     * @return the corresponding {@link CareerRankList} enum constant
     * @throws IllegalArgumentException if the rank index is out of bounds
     */
    public static CareerRankList fromRank(int rank) {
        CareerRankList[] values = values();
        if (rank < 1 || rank > values.length) {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
        return values[rank - 1];
    }

    /**
     * Total number of ranks available.
     *
     * @return the count of ranks
     */
    public static int count() {
        return values().length;
    }
}
