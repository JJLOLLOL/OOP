package models.career;

/**
 * Represents a Sim's current occupation.
 * Tracks their selected career path, rank, and XP progress towards the next promotion.
 */
public class Career {

    private final CareerList currentCareer;
    private int currentRank;
    private double progress;
    private double requiredXP;

    /**
     * Constructs a new Career path for a Sim.
     *
     * @param currentCareer the {@link CareerList} enum representing the chosen job
     */
    public Career(CareerList currentCareer) {
        this.currentCareer = currentCareer;
        this.currentRank = 1;
        this.progress = 0.0;
        this.requiredXP = 100.0;
    }

    /**
     * Retrieves the specific career enum associated with this instance.
     *
     * @return the {@link CareerList} enum
     */
    public CareerList getCurrentCareer() {
        return currentCareer;
    }

    /**
     * Retrieves the base title of the career.
     *
     * @return the title of the career
     */
    public String getTitle() {
        return currentCareer.getTitle();
    }

    /**
     * Retrieves the standard working hours required for this career.
     *
     * @return the working hours
     */
    public double getWorkingHours() {
        return currentCareer.getWorkingHours();
    }

    /**
     * Retrieves the descriptive title of the current rank (e.g., "Intern", "Manager").
     *
     * @return the string representation of the current rank
     */
    public String getRank() {
        return CareerRankList.fromRank(currentRank).getTitle();
    }

    /**
     * Retrieves the numeric level of the current rank.
     *
     * @return the current rank number
     */
    public int getCurrentRank() {
        return currentRank;
    }

    /**
     * Calculates the current salary based on the base salary and rank multiplier.
     *
     * @return the adjusted salary
     */
    public double getSalary() {
        return currentCareer.getBaseSalary() * CareerRankList.fromRank(currentRank).getSalaryMultiplier();
    }

    /**
     * Retrieves the current accumulated XP in this career.
     *
     * @return the current progress
     */
    public double getProgress() {
        return progress;
    }

    /**
     * Retrieves the XP required to reach the next rank.
     *
     * @return the required XP
     */
    public double getRequiredXP() {
        return requiredXP;
    }

    /**
     * Adds XP progress to the career. If enough progress is accumulated,
     * handles the logic to promote the Sim to the next rank.
     *
     * @param amount the amount of XP to add
     * @return a message detailing the current progress or promotion status
     */
    public String addProgress(double amount) {
        if (currentRank >= CareerRankList.count()) {
            return "Max rank attained! Cannot gain anymore XP.";
        }
        progress += amount;
        if (progress >= requiredXP) {
            progress -= requiredXP;
            currentRank++;
            requiredXP = 100.0 * Math.pow(1.5, currentRank - 1);
            return "Promoted to " + getRank() + " | Next rank requires: " + requiredXP + " XP";
        }
        return "Progress: " + progress + " / " + requiredXP + " XP";
    }
}
