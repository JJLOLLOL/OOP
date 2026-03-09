package models;

public class Career implements ProgressBar {
    private CareerList currentCareer;
    private int currentRank;
    private double salary;
    private double progress;

    public Career(CareerList currentCareer) {
        this.currentCareer = currentCareer;
        this.currentRank = 1;
        this.progress = 0.0;
    }

    // Getter and Setter methods
    public String getTitle() {
        return currentCareer.getTitle();
    }
    public double getSalary() {
        return salary * CareerRank.getSalaryMultiplier(currentRank);
    }
    public double getWorkingHours() {
        return currentCareer.getWorkingHours();
    }
    public String getRank(){
        return CareerRank.getTitle(currentRank);
    }
    public void setRank(int rank) {
        this.currentRank = rank;
    }

    //abstract methods from ProgressBar
    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public void addProgress(double amount){
        this.progress += amount;
        if (this.progress >= 100.0) {
            this.currentRank++;
            this.progress -= 100.0; // resets progress
        }
    }
}