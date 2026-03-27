package models.career;

/**
 * Enumerates the rank ladder and salary multiplier for career progression.
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

    CareerRankList(String title, double salaryMultiplier) {
        this.title = title;
        this.salaryMultiplier = salaryMultiplier;
    }

    public String getTitle() {
        return title;
    }

    public double getSalaryMultiplier() {
        return salaryMultiplier;
    }

    /**
     * Returns the rank enum for a 1-based rank index.
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
     */
    public static int count() {
        return values().length;
    }
}
