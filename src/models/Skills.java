

public class Skills{
    public String skillname;
    public int level;
    public double progress;

    public Skills(String skillname){
        this.skillname=skillname;
        this.level=1;
        this.progress=0.0;
    }
    public String getSkillname(){
        return skillname;
    }
    public int getLevel(){
        return level;
    }
    public double getProgress(){
        return progress;
    }
}
