package models;

public class Career implements ProgressBar{
    public  String title;
    public double salary;
    public double duration; //in hours to represent duration the Sim is at work
    public double progress;
    public int level;

    public Career(String title, double salary, double duration) {
        this.title = title;
        this.salary = salary;
        this.level = 1;
        this.duration = duration;
        this.progress = 0.0;
    }

    public String getTitle() {
        return title;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public void addProgress(double amount){
        this.progress += amount;
        if (this.progress >= 100.0) {
            this.level++;
            this.progress -= 100.0;
        }
    }

    public double getDuration() {
        return duration;
    }
}