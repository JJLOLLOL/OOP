package models.career;

import models.skill.SkillType;

public class Career {

    private static final int SHIFT_START_HOUR = 9;
    private static final double BASE_REQUIRED_XP = 100.0;
    private static final double XP_GROWTH_MULTIPLIER = 1.5;

    private final CareerList currentCareer;
    private int currentRank;
    private double progress;
    private double requiredXP;

    public Career(CareerList currentCareer) {
        if (currentCareer == null) {
            throw new IllegalArgumentException("Career cannot be null.");
        }

        this.currentCareer = currentCareer;
        this.currentRank = 1;
        this.progress = 0.0;
        this.requiredXP = BASE_REQUIRED_XP;
    }

    public CareerList getCurrentCareer() {
        return currentCareer;
    }

    public String getTitle() {
        return currentCareer.getTitle();
    }

    public boolean isJobless() {
        return currentCareer == CareerList.JOBLESS;
    }

    public double getWorkingHours() {
        return currentCareer.getWorkingHours();
    }

    public int getShiftStartHour() {
        return SHIFT_START_HOUR;
    }

    public int getShiftEndHour() {
        return SHIFT_START_HOUR + (int) getWorkingHours();
    }

    public boolean hasShiftStarted(double currentTime) {
        return currentTime >= getShiftStartHour();
    }

    public boolean isShiftOver(double currentTime) {
        return currentTime >= getShiftEndHour();
    }

    public double getRemainingShiftHours(double currentTime) {
        if (!hasShiftStarted(currentTime) || isShiftOver(currentTime)) {
            return 0.0;
        }
        return getShiftEndHour() - currentTime;
    }

    public double getWorkFraction(double hoursWorked) {
        double fullShift = getWorkingHours();
        if (fullShift <= 0) {
            return 0.0;
        }
        return hoursWorked / fullShift;
    }

    public String getRank() {
        return CareerRankList.fromRank(currentRank).getTitle();
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public double getSalary() {
        return currentCareer.getBaseSalary()
                * CareerRankList.fromRank(currentRank).getSalaryMultiplier();
    }

    public double calculatePay(double hoursWorked) {
        return getSalary() * getWorkFraction(hoursWorked);
    }

    public SkillType[] getRelatedSkills() {
        return currentCareer.getRelatedSkills();
    }

    public double getProgress() {
        return progress;
    }

    public double getRequiredXP() {
        return requiredXP;
    }

    public PromotionStatus addProgress(double amount) {
        if (amount <= 0) {
            return PromotionStatus.NONE;
        }
    
        if (currentRank >= CareerRankList.count()) {
            return PromotionStatus.MAX_RANK;
        }
    
        progress += amount;
    
        if (progress >= requiredXP) {
            progress -= requiredXP;
            currentRank++;
            requiredXP = BASE_REQUIRED_XP * Math.pow(XP_GROWTH_MULTIPLIER, currentRank - 1);
            return PromotionStatus.PROMOTED;
        }
    
        return PromotionStatus.NONE;
    }
}