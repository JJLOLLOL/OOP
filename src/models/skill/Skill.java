package models.skill;

import models.ProgressBar;

/**
 * Represents a specific skill that a Sim can level up over time.
 * Skills implement the {@link ProgressBar} interface to track experience points and levels.
 */
public class Skill implements ProgressBar{
    private String skillName;
    private int level;
    private double progress;
    private double requiredXP;
    private static final int MAX_LEVEL = 10;

    /**
     * Constructs a new Skill with the specified name, starting at level 1.
     *
     * @param skillName the name of the skill (e.g., "Cooking", "Fitness")
     */
    public Skill(String skillName){
        this.skillName = skillName;
        this.level = 1;
        this.progress = 0.0;
        this.requiredXP = 100.0;
    }

    /**
     * Retrieves the name of the skill.
     *
     * @return the skill name
     */
    public String getSkillName(){
        return skillName;
    }

    /**
     * Retrieves the current level of the skill.
     *
     * @return the skill level
     */
    public int getLevel(){
        return level;
    }

    @Override
    public double getRequiredXP() {
        return requiredXP;
    }

    /**
     * Recalculates the required XP for the next level based on the current level.
     */
    private void updateRequiredXP() {
        requiredXP = 100.0 * Math.pow(1.5, level - 1);
    }
    @Override
    public double getProgress(){
        return progress;
    }

    @Override
    public String addProgress(double amount){
        this.progress = Math.max(0, this.progress + amount);
        if (this.progress >= requiredXP) {
            if (this.level >= MAX_LEVEL) {
                this.progress = 0;
                return skillName + " is already at max level!";
            }
            this.progress -= requiredXP;
            this.level++;
            updateRequiredXP();
            return skillName + " levelled up! Level: " + level
                    + " | Next level requires: " + requiredXP + " XP";
        }
        return skillName + " | Progress: " + progress + " / " + requiredXP + " XP";
    }

    @Override
    public String toString() {
        return skillName +
                " | Level: "    + level +
                " | Progress: " + progress + " / " + requiredXP + " XP";
    }

}
