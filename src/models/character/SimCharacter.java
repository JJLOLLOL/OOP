package models.character;

import Types.CareerList;
import models.career.Career;
import models.debuffs.DebuffRegistry;
import models.location.House;
import models.location.Location;
import models.need.*;
import models.skill.Skill;
import models.skill.SkillType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class SimCharacter extends Character {

    private double money;
    private final Map<NeedType, Need> needs = new HashMap<>();
    private final Map<SkillType, Skill> skills = new HashMap<>();
    private Career career;
    private House currentHouse;

    public SimCharacter(String name, int age, String gender, Location defaultLocation) {
        super(name, age, gender, defaultLocation);
        this.money = 1000.0;
        this.currentHouse = null;
        this.career = new Career(CareerList.JOBLESS);
        initialiseNeeds();
        initialiseSkills();
    }

    private void initialiseNeeds() {
        needs.put(NeedType.HUNGER, new Hunger());
        needs.put(NeedType.HYGIENE, new Hygiene());
        needs.put(NeedType.ENERGY, new Energy());
        needs.put(NeedType.FUN, new Fun());
        needs.put(NeedType.SOCIAL, new Social());
    }

    private void initialiseSkills() {
        skills.put(SkillType.COOKING, new Skill(SkillType.COOKING));
        skills.put(SkillType.FITNESS, new Skill(SkillType.FITNESS));
        skills.put(SkillType.PROGRAMMING, new Skill(SkillType.PROGRAMMING));
        skills.put(SkillType.CHARISMA, new Skill(SkillType.CHARISMA));
        skills.put(SkillType.CREATIVITY, new Skill(SkillType.CREATIVITY));
        skills.put(SkillType.LOGIC, new Skill(SkillType.LOGIC));
        skills.put(SkillType.MUSIC, new Skill(SkillType.MUSIC));
        skills.put(SkillType.WRITING, new Skill(SkillType.WRITING));
        skills.put(SkillType.PAINTING, new Skill(SkillType.PAINTING));
    }

    // skills
    public Skill getSkill(SkillType type) {
        if (type == null) {
            throw new IllegalArgumentException("Skill type cannot be null.");
        }
        return skills.get(type);
    }
    public int gainSkillXp(SkillType type, double xp) {
        if (type == null) {
            throw new IllegalArgumentException("Skill type cannot be null.");
        }
        if (xp < 0) {
            throw new IllegalArgumentException("XP gained cannot be negative.");
        }

        Skill skill = skills.get(type);
        if (skill == null) {
            throw new IllegalStateException("Skill not found: " + type);
        }
        return skill.addProgress(xp);
    }
    public int getSkillLevel(SkillType type) {
        return getSkill(type).getLevel();
    }
    public double getSkillXp(SkillType type) {
        return getSkill(type).getProgress();
    }

    // TODO: encapsulation is stable but not strong enough
    public Collection<Skill> getSkillViews() {
        return Collections.unmodifiableCollection(skills.values());
    }
    
    public String addSkillProgress(SimCharacter sim, SkillType type, double amount) {
        double modified = DebuffRegistry.applySkillModifiers(this, type, amount);
        Skill skill = skills.get(type);
        return "progressed: " + skill.addProgress(modified);
    }
    
    // needs
    public Need getNeed(NeedType type) {
        if (type == null) {
            throw new IllegalArgumentException("Need type cannot be null.");
        }
    
        Need need = needs.get(type);
        if (need == null) {
            throw new IllegalStateException("Need not found: " + type);
        }
    
        return need;
    }
    
    public void adjustNeed(NeedType type, int amount) {
        if (type == null) {
            throw new IllegalArgumentException("Need type cannot be null.");
        }
        if (amount == 0) {
            return;
        }

        Need need = getNeed(type);
        need.adjust(amount);
    }

    public void adjustNeedNS(SimCharacter sim, NeedType type, double amount) {
        double modified = DebuffRegistry.applyNeedModifiers(this, type, amount);
        Need need = needs.get(type);
        if (need != null) {
            need.adjustValue(modified);
        }
    }

    @Deprecated
    public void updateNeeds(double deltaTime) {
        for (Need need : this.getNeeds().values()) {
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
    
    private Map<NeedType, Need> getNeeds() {
        return needs;
    }
    
    // TODO: encapsulation is stable but not strong enough
    public Collection<Need> getNeedViews() {
        return Collections.unmodifiableCollection(needs.values());
    }

    public Career getCareer() {
        return career;
    }

    public void joinCareer(CareerList newCareer) {
        this.career = new Career(newCareer);
    }


    public void setMoney(double amount) {
        money += amount;
    }
    public double getMoney() {
        return money;
    }
    public House getCurrentHouse() {
        return currentHouse;
    }
    public void setCurrentHouse(House house) {
        this.currentHouse = house;
    }
}
