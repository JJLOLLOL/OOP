package models;

public class Skills implements ProgressBar{
    public String skillName;
    public int level;
    public double progress;
    public String[] skills_array;
    public Skills(String skillName){
        this.skillName = skillName;
        this.level = 1;
        this.progress = 0.0;
    }

    public String getSkillName(){
        return skillName;
    }

    public int getLevel(){
        return level;
    }

    @Override
    public double getProgress(){
        return progress;
    }

    @Override
    public void addProgress(double amount){
        this.progress += amount;
        if (this.progress >= 100.0) {
            this.level++;
            this.progress = 0.0;
        }
    }

}
