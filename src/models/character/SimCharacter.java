package models.character;

import java.util.Map;

import javax.swing.Action;

import core.ActionResult;
import core.GameClock;
import models.actions.Furniture;
import models.actions.FurnitureAction;
import models.career.Career;
import models.career.CareerList;
import models.career.PromotionStatus;
import models.character.finances.CharacterFinances;
import models.character.housing.CharacterHousing;
import models.character.stats.CharacterStats;
import models.debuffs.DebuffRegistry;
import models.location.House;
import models.location.Location;
import models.need.*;
import models.skill.SkillType;



public class SimCharacter extends Character {

    private final CharacterStats stats;
    private final CharacterFinances finances;
    private final CharacterHousing housing;
    private Career career;
    private static final double CAREER_XP_PER_SHIFT = 20.0;
    private static final double SKILL_XP_PER_HOUR = 5.0;

    private static FurnitureAction WORK_ACTION;

    public SimCharacter(String name, int age, String gender, Location defaultLocation) {
        super(name, age, gender, defaultLocation);

        this.stats = new CharacterStats();
        this.finances = new CharacterFinances();
        this.housing = new CharacterHousing();
        this.career = new Career(CareerList.JOBLESS);
    }

    /**
     * Sets the global work action. This should be called once after data is loaded.
     * @param action The action defining the effects of working a shift.
     */
    public static void setWorkAction(FurnitureAction action) {
        WORK_ACTION = action;
    }

    // ======== ????
    public int getAge() {
        return super.getAge();
    }
    public String getGender() {
        return super.getGender();
    }

    // ======== STATS
    public CharacterStats getStats() {
        return stats;
    }
    public Need getNeed(NeedType type) {
        return stats.getNeed(type);
    }
    public int adjustSkillXp(SkillType type, double amount) {
        if (type == null) {
            throw new IllegalArgumentException("Skill type cannot be null.");
        }
        if (amount == 0) {
            return 0;
        }
        double finalAmount = DebuffRegistry.applySkillModifiers(this, type, amount);
        return stats.adjustSkillXpRaw(type, finalAmount);
    }
    public void adjustNeed(NeedType type, double amount) {
        if (type == null) {
            throw new IllegalArgumentException("Need type cannot be null.");
        }
        if (amount == 0) {
            return;
        }
        double finalAmount = DebuffRegistry.applyNeedModifiers(this, type, amount);
        stats.adjustNeedRaw(type, finalAmount);
    }

    public void updateNeeds(double deltaTime) {
        for (Need need : stats.getNeedViews()) {
            double effectiveDecay = DebuffRegistry.applyDecayModifiers(this, need.getType(), need.getBaseDecayRate());
            need.update(this, deltaTime, effectiveDecay);
        }
    }

    // ======== FINANCES
    public void spendMoney(double amount) {
        finances.spendMoney(amount);
    }
    public void earnMoney(double amount) {
        finances.earnMoney(amount);
    }
    public double getMoney() {
        return finances.getMoney();
    }
    public boolean canAfford(double amount) {
        return finances.canAfford(amount);
    }

    // ======== CAREER
    public Career getCareer() {
        return career;
    }

    public void joinCareer(CareerList newCareer) {
        if (newCareer == null) {
            throw new IllegalArgumentException("Career cannot be null.");
        }
        this.career = new Career(newCareer);
    }

    public boolean isJobless() {
        return career.isJobless();
    }


    public ActionResult work(GameClock clock) {
        if (career.isJobless()) {
            return ActionResult.failure("You need a job before you can work!");
        }

        double currentTime = clock.getHours() + clock.getMinutes() / 60.0;
        if (!career.hasShiftStarted(currentTime)) {
            return ActionResult.failure(String.format("Work doesn't start until %02d:00.", career.getShiftStartHour()));
        }
        if (career.isShiftOver(currentTime)) {
            return ActionResult.failure(String.format("he work day is over (shift ends %02d:00). Come back tomorrow!", career.getShiftEndHour()));
        }

        double hoursWorked = career.getRemainingShiftHours(currentTime);
        double workFraction = career.getWorkFraction(hoursWorked);

        clock.advanceHours(hoursWorked);

        applyWorkNeedEffects(workFraction);

        double earned = career.calculatePay(hoursWorked);
        earnMoney(earned);

        PromotionStatus promotionStatus = career.addProgress(CAREER_XP_PER_SHIFT * workFraction);

        applyWorkSkillXp(hoursWorked);

        StringBuilder message = new StringBuilder();
        message.append(String.format(
                "Worked %.1f / %.0f hours. Earned $%.2f.",
                hoursWorked,
                career.getWorkingHours(),
                earned));

        if (promotionStatus == PromotionStatus.PROMOTED) {
            message.append("\nPromoted to ").append(career.getRank()).append("!");
        } else if (promotionStatus == PromotionStatus.MAX_RANK) {
            message.append("\nAlready at maximum rank.");
        }

        return ActionResult.success(message.toString());
    }
    private void applyWorkNeedEffects(double workFraction) {
        if (WORK_ACTION == null) {
            throw new IllegalStateException("Work action has not been initialized. Ensure DataParser has set this value.");
        }
        for (Map.Entry<NeedType, Double> entry : WORK_ACTION.affectedNeedsByActionMap().entrySet()) {
            adjustNeed(entry.getKey(), entry.getValue() * workFraction);
        }
    }

    private void applyWorkSkillXp(double hoursWorked) {
        for (SkillType skill : career.getRelatedSkills()) {
            adjustSkillXp(skill, SKILL_XP_PER_HOUR * hoursWorked);
        }
    }

    // ======== HOUSING
    public House getCurrentHouse() {
        return housing.getCurrentHouse();
    }
    public void assignHouse(House house) {
        housing.assignHouse(house);;
    }

    public ActionResult purchaseHouse(House targetHouse) {
        CharacterHousing.HousingResult result = housing.buyHouse(targetHouse, finances);
        switch (result) {
            case SUCCESS: return ActionResult.success(getName() + " bought " + targetHouse.getLocationName() + " for $" + targetHouse.getPrice());
            case INSUFFICIENT_FUNDS: return ActionResult.failure("Insufficient funds! Need $" + targetHouse.getPrice() + ", have: $" + getMoney());
            default: throw new IllegalStateException("Unexpected result: " + result);
        }
    }

    public ActionResult buyFurniture(Furniture furniture) {
        CharacterHousing.HousingResult result = housing.buyFurniture(furniture, finances);
        switch (result) {
            case SUCCESS: return ActionResult.success(getName() + " bought " + furniture.getName() + " for $" + furniture.getPrice());
            case HOUSE_FULL: return ActionResult.failure("Your house is at maximum furniture capacity.");
            case INSUFFICIENT_FUNDS: return ActionResult.failure("Insufficient funds! Need $" + furniture.getPrice() + ", have: $" + getMoney());
            default: throw new IllegalStateException("Unexpected result: " + result);
        }
    }
    public ActionResult sellFurniture(Furniture furniture) {
        CharacterHousing.HousingResult result = housing.sellFurniture(furniture, finances);
        switch (result) {
            case SUCCESS: return ActionResult.success(getName() + " sold " + furniture.getName() + " for $" + furniture.getPrice());
            case HOUSE_EMPTY: return ActionResult.failure("Your house is empty.");
            default: throw new IllegalStateException("Unexpected result: " + result);
        }
    }
    
    public String getPurchaseMessage(House house, boolean success) {
        if (!success) {
            return "Insufficient funds! You need $" + house.getPrice() + " to purchase this house.";
        }
        return getName() + " purchased " + house.getLocationName() + " for $" + house.getPrice() + "!";
    }

}
