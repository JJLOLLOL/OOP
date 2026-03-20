package models;

import Types.CareerList;
import Types.CareerRankList;

public class Career {

    private final CareerList currentCareer;
    private int currentRank;
    private double progress;
    private double requiredXP;

    public Career(CareerList currentCareer) {
        this.currentCareer = currentCareer;
        this.currentRank = 1;
        this.progress = 0.0;
        this.requiredXP = 100.0;
    }

    public CareerList getCurrentCareer() {
        return currentCareer;
    }

    public String getTitle() {
        return currentCareer.getTitle();
    }

    public double getWorkingHours() {
        return currentCareer.getWorkingHours();
    }

    public String getRank() {
        return CareerRankList.fromRank(currentRank).getTitle();
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public double getSalary() {
        return currentCareer.getBaseSalary() * CareerRankList.fromRank(currentRank).getSalaryMultiplier();
    }

    public double getProgress() {
        return progress;
    }

    public double getRequiredXP() {
        return requiredXP;
    }

    public String addProgress(double amount) {
        if (currentRank >= CareerRankList.count()) {
            return "Max rank attained! Cannot gain anymore XP.";
        }
        progress += amount;
        if (progress >= requiredXP) {
            progress -= requiredXP;
            currentRank++;
            requiredXP = 100.0 * Math.pow(1.5, currentRank - 1);
            return "Promoted to " + getRank() + " | Next rank requires: " + requiredXP + " XP";
        }
        return "Progress: " + progress + " / " + requiredXP + " XP";
    }
}
