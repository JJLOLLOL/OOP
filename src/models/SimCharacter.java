package models;

import java.util.HashMap;
import java.util.Map;
import models.furnitureactions.Furniture;
import models.needs.*;

public class SimCharacter extends Character {
    private boolean currentlyPlaying;
    private double money;
    private House house;
    private Map<String, Need> needs = new HashMap<>();
    private HashMap<String, Skills> skills = new HashMap<>();
    private Career career;

    public SimCharacter(String name, int age, String gender, Location defaultLocation, Career career) {
        super(name, age, gender, defaultLocation);
        this.money = 1000.0;
        this.career = career;
        initialiseNeeds();
        initialiseSkills();
    }

    private void initialiseNeeds() {
        needs.put("Hunger", new Hunger());
        needs.put("Hygiene", new Hygiene());
        needs.put("Energy", new Energy());
        needs.put("Fun", new Fun());
        needs.put("Social", new Social());
    }

    private void initialiseSkills() {
        skills.put("Cooking", new Skills("Cooking"));
        skills.put("Fitness", new Skills("Fitness"));
        skills.put("Programming", new Skills("Programming"));
        skills.put("Charisma", new Skills("Charisma"));
        skills.put("Creativity", new Skills("Creativity"));
        skills.put("Logic", new Skills("Logic"));
        skills.put("Gardening", new Skills("Gardening"));
        skills.put("Music", new Skills("Music"));
        skills.put("Writing", new Skills("Writing"));
        skills.put("Painting", new Skills("Painting"));
    }

    // getters & setters
    public void setMoney(double amount) {
        money += amount;
    }

    public double getMoney() {
        return money;
    }

    public Map<String, Need> getNeeds() {
        return needs;
    }

    public Map<String, Skills> getSkills() {
        return skills;
    }

    public void adjustNeed(String needName, double amount) {
        Need need = needs.get(needName);
        if (need != null) {
            need.adjustNeed(amount);
        }
    }

    public void addSkillProgress(String skillName, double amount) {
        Skills skill = skills.computeIfAbsent(skillName, Skills::new);
        skill.addProgress(amount);
    }

    public boolean performFurnitureActivity(Furniture furniture, String actionName) {
        if (furniture == null || actionName == null || actionName.isBlank()) {
            return false;
        }
        return furniture.performAction(actionName, this);
    }
}