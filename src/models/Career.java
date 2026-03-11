package models;

public class Career implements ProgressBar {
    private CareerList currentCareer;
    private int currentRank;
    private double progress;
    private double requiredXP;

    public Career(CareerList currentCareer) {
        this.currentCareer = currentCareer;
        this.currentRank = 1;
        this.progress = 0.0;
        this.requiredXP = 100.0;
    }

    // Getter and Setter methods
    public String getTitle() {
        return currentCareer.getTitle();
    }
    public double getSalary() {
        return currentCareer.getBaseSalary() * CareerRank.getSalaryMultiplier(currentRank);
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
    public double getRequiredXP() {
        return requiredXP;
    }

    //increase required XP needed per level
    private void updateRequiredXP() {
        requiredXP = 100.0 * Math.pow(1.5, currentRank - 1);
    }
    //abstract methods from ProgressBar
    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public String addProgress(double amount){
        if (currentRank >= CareerRank.RANK.length){
            return "Max rank attained! Cannot gain anymore XP.";
        }
        this.progress += amount;
        if (this.progress >= requiredXP) {
            this.progress -= requiredXP;
            currentRank++;
            updateRequiredXP();
            return "Promoted to " + getRank()
                    + " | Next rank requires: " + requiredXP + " XP";
        }
        return "Progress: " + progress + " / " + requiredXP + " XP";
    }

    @Override
    public String toString() {
        return "=== Career Info ===" +
                "\nTitle:         " + getTitle() +
                "\nRank:          " + getRank() +
                "\nSalary:        " + getSalary() +
                "\nWorking Hours: " + getWorkingHours() +
                "\nProgress:      " + progress + " / " + requiredXP + " XP";
    }
}