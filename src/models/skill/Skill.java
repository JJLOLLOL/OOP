package models.skill;

import models.progression.XpTracker;

public class Skill {
    private final SkillType type;
    private int level;
    private final int maxLevel;
    private final XpTracker xpTracker;

    public Skill(SkillType type) {
        this(type, 1, 10);
    }

    public Skill(SkillType type, int startingLevel, int maxLevel) {
        if (type == null) {
            throw new IllegalArgumentException("Skill type cannot be null.");
        }
        if (startingLevel < 1) {
            throw new IllegalArgumentException("Starting level must be at least 1.");
        }
        if (maxLevel < 1 || startingLevel > maxLevel) {
            throw new IllegalArgumentException("Invalid max level.");
        }
        this.type = type;
        this.level = startingLevel;
        this.maxLevel = maxLevel;
        this.xpTracker = new XpTracker(calculateRequiredXP(level));
    }

    public SkillType getType() {
        return type;
    }

    public String getName() {
        return type.getName();
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public double getProgress() {
        return xpTracker.getProgress();
    }

    public double getRequiredXP() {
        return xpTracker.getRequiredXP();
    }

    public boolean isMaxLevel() {
        return level >= maxLevel;
    }

    public int addProgress(double amount) {
        if (amount == 0) {
            return 0;
        }
        if (amount > 0 && isMaxLevel()) {
            return 0;
        }

        xpTracker.addProgress(amount);

        int levelsGained = 0;

        while (!isMaxLevel() && xpTracker.canAdvance()) {
            xpTracker.consumeRequiredXP();
            level++;
            levelsGained++;

            if (!isMaxLevel()) {
                xpTracker.setRequiredXP(calculateRequiredXP(level));
            } else {
                xpTracker.resetProgress();
            }
        }

        return levelsGained;
    }

    private double calculateRequiredXP(int level) {
        return 100.0 * Math.pow(1.5, level - 1);
    }
    
    @Override
    public String toString() {
        return type + " | Level: " + level + " | XP: " + getProgress() + "/" + getRequiredXP();
    }
}