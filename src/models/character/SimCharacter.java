package models.character;

import java.util.Map;

import core.ActionResult;
import core.GameClock;
import models.career.Career;
import models.career.CareerList;
import models.career.PromotionStatus;
import models.character.finances.CharacterFinances;
import models.character.housing.CharacterHousing;
import models.character.stats.CharacterStats;
import models.debuffs.DebuffRegistry;
import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.location.House;
import models.location.Location;
import models.need.*;
import models.skill.SkillType;
import types.Gender;

/**
 * Playable sim implementation combining needs, skills, finances, career, and
 * housing state.
 */
public class SimCharacter extends Character {

    private final CharacterStats stats;
    private final CharacterFinances finances;
    private final CharacterHousing housing;
    private Career career;
    private static final double CAREER_XP_PER_SHIFT = 20.0;
    private static final double SKILL_XP_PER_HOUR = 5.0;

    private static FurnitureAction WORK_ACTION;

    /**
     * Creates a playable sim with default stats, finances, housing state, and
     * no career.
     *
     * @param name the sim's name
     * @param age the sim's age
     * @param gender the sim's gender
     * @param defaultLocation the sim's starting location
     */
    public SimCharacter(String name, int age, Gender gender, Location defaultLocation) {
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
        if (action == null) {
            throw new IllegalArgumentException("Work action cannot be null.");
        }
        WORK_ACTION = action;
    }

    // ======== STATS
    public CharacterStats getStats() {
        return stats;
    }
    public Need getNeed(NeedType type) {
        return stats.getNeed(type);
    }

    /**
     * Applies a skill XP delta after all active debuff modifiers and returns
     * the number of levels gained.
     *
     * @param type the skill being modified
     * @param amount the raw XP delta
     * @return the number of levels gained
     */
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

    /**
     * Applies a need delta after all active debuff modifiers.
     *
     * @param type the need being modified
     * @param amount the raw need delta
     */
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

    /**
     * Advances every tracked need for one update step using the debuff-adjusted
     * decay rate for each need.
     *
     * @param minutesPassed the elapsed in-game time in minutes
     */
    public void updateNeeds(int minutesPassed) {
        if (minutesPassed <= 0) {
            return;
        }

        for (Need need : stats.getNeedViews()) {
            double effectiveDecay = DebuffRegistry.applyDecayModifiers(this, need.getType(), need.getBaseDecayRate());
            need.update(this, minutesPassed, effectiveDecay);
        }
    }

    // ======== FINANCES

    /**
     * Spends money from the sim's finances.
     *
     * @param amount the amount to spend
     */
    public void spendMoney(double amount) {
        finances.spendMoney(amount);
    }

    /**
     * Adds money to the sim's finances.
     *
     * @param amount the amount to add
     */
    public void earnMoney(double amount) {
        finances.earnMoney(amount);
    }
    public double getMoney() {
        return finances.getMoney();
    }

    /**
     * Returns whether the sim can afford the supplied amount.
     *
     * @param amount the amount to check
     * @return {@code true} when the sim has enough money
     */
    public boolean canAfford(double amount) {
        return finances.canAfford(amount);
    }

    // ======== CAREER
    public Career getCareer() {
        return career;
    }

    /**
     * Switches the sim into a new career at rank 1 with zero progress.
     *
     * @param newCareer the career to join
     */
    public void joinCareer(CareerList newCareer) {
        if (newCareer == null) {
            throw new IllegalArgumentException("Career cannot be null.");
        }
        this.career = new Career(newCareer);
    }

    public boolean isJobless() {
        return career.isJobless();
    }

    /**
     * Works the remainder of the current shift, applying time skip, need
     * changes, pay, career progress, and related skill XP.
     *
     * @param clock the game clock used to advance in-game time
     * @return the outcome of the work action
     */
    public ActionResult work(GameClock clock) {
        if (career.isJobless()) {
            return ActionResult.failure("You need a job before you can work!");
        }

        double currentTime = clock.getHours() + clock.getMinutes() / 60.0;
        if (!career.hasShiftStarted(currentTime)) {
            return ActionResult.failure(String.format("Work doesn't start until %02d:00.", career.getShiftStartHour()));
        }
        if (career.isShiftOver(currentTime)) {
            return ActionResult.failure(String.format("The work day is over (shift ends %02d:00). Come back tomorrow!", career.getShiftEndHour()));
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

    /**
     * Applies the configured work-action need deltas scaled by the fraction of
     * the shift worked.
     *
     * @param workFraction the fraction of a full shift that was completed
     */
    private void applyWorkNeedEffects(double workFraction) {
        if (WORK_ACTION == null) {
            throw new IllegalStateException("Work action has not been initialized. Ensure WorldLoader has set this value.");
        }
        for (Map.Entry<NeedType, Double> entry : WORK_ACTION.affectedNeedsByActionMap().entrySet()) {
            adjustNeed(entry.getKey(), entry.getValue() * workFraction);
        }
    }

    /**
     * Awards work-related skill XP for the supplied number of hours worked.
     *
     * @param hoursWorked the number of in-game hours worked
     */
    private void applyWorkSkillXp(double hoursWorked) {
        for (SkillType skill : career.getRelatedSkills()) {
            adjustSkillXp(skill, SKILL_XP_PER_HOUR * hoursWorked);
        }
    }

    // ======== HOUSING
    public House getCurrentHouse() {
        return housing.getCurrentHouse();
    }

    /**
     * Assigns the sim's current home.
     *
     * @param house the house to assign
     */
    public void assignHouse(House house) {
        housing.assignHouse(house);;
    }

    /**
     * Attempts to purchase the supplied house definition as the sim's current
     * home.
     *
     * @param targetHouse the house being purchased
     * @return the purchase outcome
     */
    public ActionResult purchaseHouse(House targetHouse) {
        CharacterHousing.HousingResult result = housing.upgradeTo(targetHouse, finances);
        switch (result) {
            case SUCCESS: return ActionResult.success(getName() + " bought " + targetHouse.getLocationName() + " for $" + targetHouse.getPrice());
            case INSUFFICIENT_FUNDS: return ActionResult.failure("Insufficient funds! Need $" + targetHouse.getPrice() + ", have: $" + getMoney());
            default: throw new IllegalStateException("Unexpected result: " + result);
        }
    }

    /**
     * Attempts to buy one furniture item for the sim's current house.
     *
     * @param furniture the furniture being purchased
     * @return the purchase outcome
     */
    public ActionResult buyFurniture(Furniture furniture) {
        CharacterHousing.HousingResult result = housing.buyFurniture(furniture, finances);
        switch (result) {
            case SUCCESS: return ActionResult.success(getName() + " bought " + furniture.getName() + " for $" + furniture.getPrice());
            case HOUSE_FULL: return ActionResult.failure("Your house is at maximum furniture capacity.");
            case INSUFFICIENT_FUNDS: return ActionResult.failure("Insufficient funds! Need $" + furniture.getPrice() + ", have: $" + getMoney());
            default: throw new IllegalStateException("Unexpected result: " + result);
        }
    }

    /**
     * Attempts to sell one furniture item from the sim's current house.
     *
     * @param furniture the furniture being sold
     * @return the sale outcome
     */
    public ActionResult sellFurniture(Furniture furniture) {
        CharacterHousing.HousingResult result = housing.sellFurniture(furniture, finances);
        switch (result) {
            case SUCCESS:
                double refundAmount = furniture.getPrice() * 0.5;
                String message = String.format("%s sold %s for $%.2f", getName(), furniture.getName(), refundAmount);
                return ActionResult.success(message);
            case FURNITURE_NOT_FOUND: return ActionResult.failure("Your house is empty.");
            default: throw new IllegalStateException("Unexpected result: " + result);
        }
    }

    /**
     * Builds the player-facing purchase message for a house transaction.
     *
     * @param house the house being purchased
     * @param success whether the purchase succeeded
     * @return the message that should be shown to the player
     */
    public String getPurchaseMessage(House house, boolean success) {
        if (!success) {
            return "Insufficient funds! You need $" + house.getPrice() + " to purchase this house.";
        }
        return getName() + " purchased " + house.getLocationName() + " for $" + house.getPrice() + "!";
    }

}
