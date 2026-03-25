package models.progression;

public class XpTracker {
    private double progress;
    private double requiredXP;

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

    public void setRequiredXP(double requiredXP) {
        if (requiredXP <= 0) {
            throw new IllegalArgumentException("Required XP must be more than 0");
        }
        this.requiredXP = requiredXP;
    }

    public void addProgress(double amount) {
        progress = Math.max(0, progress + amount);
    }

    public boolean canAdvance() {
        return progress >= requiredXP;
    }

    public void consumeRequiredXP() {
        if (progress >= requiredXP) {
            progress -= requiredXP;
        }
    }

    public void resetProgress() {
        progress = 0.0;
    }

}