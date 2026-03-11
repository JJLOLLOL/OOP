package models;

public class Skills implements ProgressBar{
    private String skillName;
    private int level;
    private double progress;
    private double requiredXP;
    private static final int MAX_LEVEL = 10;

    public Skills(String skillName){
        this.skillName = skillName;
        this.level = 1;
        this.progress = 0.0;
        this.requiredXP = 100.0;
    }

    //getter methods
    public String getSkillName(){
        return skillName;
    }

    public int getLevel(){
        return level;
    }

    public double getRequiredXP() {
        return requiredXP;
    }

    private void updateRequiredXP() {
        requiredXP = 100.0 * Math.pow(1.5, level - 1);
    }
    @Override
    public double getProgress(){
        return progress;
    }

    @Override
    public String addProgress(double amount){
        if (this.level >= MAX_LEVEL) {
            return skillName + " is already at max level!";
        }
        this.progress += amount;
        if (this.progress >= requiredXP) {
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
