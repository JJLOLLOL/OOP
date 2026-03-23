package models;

/**
 * Defines a contract for entities that track progress towards a goal.
 * Typically used for skills or careers where a certain amount of experience
 * points (XP) is required to level up or rank up.
 */
public interface ProgressBar {

    /**
     * Retrieves the current amount of progress (XP) accumulated.
     *
     * @return the current progress
     */
    double getProgress();

    /**
     * Adds the specified amount to the current progress.
     *
     * @param amount the amount of progress to add
     * @return a message describing the result of the addition (e.g., a level up notification)
     */
    String addProgress(double amount);

    /**
     * Retrieves the total amount of progress required to reach the next level or rank.
     *
     * @return the required XP
     */
    double getRequiredXP();
}
