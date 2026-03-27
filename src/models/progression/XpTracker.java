package models.progression;

/**
 * Reusable helper that stores current progress toward a required XP threshold.
 */
public class XpTracker {
    private double progress;
    private double requiredXP;

    /**
     * Creates a tracker with zero progress and the supplied XP requirement.
     *
     * @param requiredXP the XP required before the tracker can advance
     */
    public XpTracker(double requiredXP) {
        if (requiredXP <= 0) {
            throw new IllegalArgumentException("Required XP must be more than 0");
        }
        this.progress = 0;
        this.requiredXP = requiredXP;
    }

    public double getProgress() {
        return progress;
    }

    public double getRequiredXP() {
        return requiredXP;
    }

    /**
     * Replaces the XP threshold required for the next advancement.
     *
     * @param requiredXP the new required XP value
     */
    public void setRequiredXP(double requiredXP) {
        if (requiredXP <= 0) {
            throw new IllegalArgumentException("Required XP must be more than 0");
        }
        this.requiredXP = requiredXP;
    }

    /**
     * Adds progress while keeping the stored value non-negative.
     *
     * @param amount the progress delta to apply
     */
    public void addProgress(double amount) {
        progress = Math.max(0, progress + amount);
    }

    /**
     * Returns whether the stored progress has reached the current threshold.
     *
     * @return {@code true} when advancement is possible
     */
    public boolean canAdvance() {
        return progress >= requiredXP;
    }

    /**
     * Consumes one threshold's worth of progress after an advancement.
     */
    public void consumeRequiredXP() {
        if (progress >= requiredXP) {
            progress -= requiredXP;
        }
    }

    /**
     * Clears all stored progress back to zero.
     */
    public void resetProgress() {
        progress = 0.0;
    }

}
