package models;

import Types.CareerList;
import java.util.HashMap;
import java.util.Map;
import models.needs.*;

public class SimCharacter extends Character {

    private double money;
    private final Map<String, Need> needs = new HashMap<>();
    private final Map<String, Skills> skills = new HashMap<>();
    private Career career;

    public SimCharacter(String name, int age, String gender, Location defaultLocation) {
        super(name, age, gender, defaultLocation);
        this.money = 1000.0;
        this.career = new Career(CareerList.JOBLESS);
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
        skills.put("Music", new Skills("Music"));
        skills.put("Writing", new Skills("Writing"));
        skills.put("Painting", new Skills("Painting"));
    }

    // ── Career ────────────────────────────────────────────────────────────────
    public Career getCareer() {
        return career;
    }

    public void joinCareer(CareerList newCareer) {
        this.career = new Career(newCareer);
    }

    // ── Skills ────────────────────────────────────────────────────────────────
    public Map<String, Skills> getAllSkills() {
        return skills;
    }

    // ── Needs ─────────────────────────────────────────────────────────────────
    public Map<String, Need> getNeeds() {
        return needs;
    }

    // ── Money ─────────────────────────────────────────────────────────────────
    public void setMoney(double amount) {
        money += amount;
    }

    public double getMoney() {
        return money;
    }
}
