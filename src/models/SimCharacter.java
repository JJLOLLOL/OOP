package models;

import Types.CareerList;
import Types.SkillsList;
import java.util.HashMap;
import java.util.Map;
import models.needs.*;

public class SimCharacter extends Character {

    private double money;
    private final Map<String, Need> needs = new HashMap<>();
    private final SkillsList skillsList = new SkillsList();
    private Career career;

    public SimCharacter(String name, int age, String gender, Location defaultLocation) {
        super(name, age, gender, defaultLocation);
        this.money = 1000.0;
        this.career = new Career(CareerList.JOBLESS);
        initialiseNeeds();
        skillsList.initialiseSkills();
    }

    private void initialiseNeeds() {
        needs.put("Hunger", new Hunger());
        needs.put("Hygiene", new Hygiene());
        needs.put("Energy", new Energy());
        needs.put("Fun", new Fun());
        needs.put("Social", new Social());
    }

    // ── Career ────────────────────────────────────────────────────────────────
    public Career getCareer() {
        return career;
    }

    public void joinCareer(CareerList newCareer) {
        this.career = new Career(newCareer);
    }

    // ── Skills ────────────────────────────────────────────────────────────────
    public HashMap<String, Skills> getAllSkills() {
        return skillsList.getAllSkills();
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
