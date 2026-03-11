package models;

import java.util.HashMap;
import java.util.Map;
import models.needs.*;

public class SimCharacter extends Character {
    private boolean currentlyPlaying;
    private double money;
    private House house;
    private Map<String, Need> needs = new HashMap<>();
    private SkillsList skillsList = new SkillsList();
    private Career career;

    public SimCharacter(String name, int age, String gender, Location defaultLocation) {
        super(name, age, gender, defaultLocation);
        this.money = 1000.0;
        this.career = new Career(CareerList.JOBLESS);
        initialiseNeeds();
    }

    private void initialiseNeeds() {
        needs.put("Hunger", new Hunger());
        needs.put("Hygiene", new Hygiene());
        needs.put("Energy", new Energy());
        needs.put("Fun", new Fun());
        needs.put("Social", new Social());
    }

    //career methods
    public String updateCareer(double amount) {
        if (career.getTitle().equals("Jobless")){
            return "Cannot gain career XP while unemployed!";
        }
        return career.addProgress(amount);
    }

    public String displayCareer() {
        return career.toString();
    }

    public void joinCareer(CareerList newCareer) {
        this.career = new Career(newCareer);
    }

    //skills methods
    public String updateSkill(String skillName, double amount) {
        Skills skill = skillsList.getSkill(skillName);
        if (skill == null) {
            return "Skill " + skillName + " not found!";
        }
        return skill.addProgress(amount);  // ✅ now returns String
    }

    public String displaySkills() {
        return skillsList.displaySkills();
    }
    // getters & setters
    public void setMoney(double amount) {
        money += amount;
    }

    public double getMoney() {
        return money;
    }
}