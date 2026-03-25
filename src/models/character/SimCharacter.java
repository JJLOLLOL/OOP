package models.character;

import Types.CareerList;
import models.career.Career;
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

    public SimCharacter(String name, int age, String gender, Location defaultLocation) {
        super(name, age, gender, defaultLocation);

        this.stats = new CharacterStats();
        this.finances = new CharacterFinances();
        this.housing = new CharacterHousing();
        this.career = new Career(CareerList.JOBLESS);

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
        double finalAmount = amount;
        if (true) {
        // TODO: implement checker isDebuffActive()
            finalAmount = DebuffRegistry.applySkillModifiers(this, type, amount);
        }
        return stats.adjustSkillXpRaw(type, finalAmount);
    }
    public void adjustNeed(NeedType type, double amount) {
        if (type == null) {
            throw new IllegalArgumentException("Need type cannot be null.");
        }
        if (amount == 0) {
            return;
        }
        double finalAmount = amount;
        if (true) {
        // TODO: implement checker isDebuffActive()
            finalAmount = DebuffRegistry.applyNeedModifiers(this, type, amount);
        }
        stats.adjustNeedRaw(type, finalAmount);
    }
    @Deprecated
    public void updateNeeds(double deltaTime) {
        for (Need need : getStats().getNeedViews()) {
            double modifiedDecay = DebuffRegistry.applyDecayModifiers(
                    this, need.getType(), need.getBaseDecayRate());
            need.setDecayRate(modifiedDecay);
            need.decay(deltaTime);
            if (need.isCritical()) {
                if (!need.hasCriticalNotificationBeenSent()) {
                    need.onCriticallyLow(this);
                    need.setCriticalNotificationSent(true);
                }
            } else {
                need.setCriticalNotificationSent(false);
            }
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
        return canAfford(amount);
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


    public House getCurrentHouse() {
        return housing.getCurrentHouse();
    }
    public void assignHouse(House house) {
        housing.assignHouse(house);;
    }
}
