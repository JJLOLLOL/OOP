package models.skill;

import models.progression.XpTracker;

/**
 * Represents one skill's level and experience progression for a character.
 */
public class Skill {
    private final SkillType type;
    private int level;
    private final int maxLevel;
    private final XpTracker xpTracker;

    /**
     * Creates a skill at level 1 with the default maximum level of 10.
     *
     * @param type the skill type
     */
    public Skill(SkillType type) {
        this(type, 1, 10);
    }

    /**
     * Creates a skill with explicit starting and maximum levels.
     *
     * @param type the skill type
     * @param startingLevel the initial skill level
     * @param maxLevel the highest level this skill can reach
     */
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

    /**
     * Applies skill experience and levels up repeatedly while enough XP
     * remains.
     *
     * @param amount the XP delta to apply
     * @return the number of levels gained
     */
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

    /**
     * Calculates the XP requirement for the supplied level using the skill
     * growth curve.
     */
    private double calculateRequiredXP(int level) {
        return 100.0 * Math.pow(1.5, level - 1);
    }

    /**
     * Returns a concise summary of the skill's current level and XP progress.
     *
     * @return a human-readable skill summary
     */
    @Override
    public String toString() {
        return type + " | Level: " + level + " | XP: " + getProgress() + "/" + getRequiredXP();
    }
}
