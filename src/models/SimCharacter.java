package models;

import java.util.HashMap;
import models.needs.*;

public class SimCharacter extends Character {
    private boolean currentlyPlaying;
    private double money;
    private House house;
    private Career career;
    private HashMap<String, Need> needs = new HashMap<>();
    private HashMap<String, Skills> skills = new HashMap<>();

    public SimCharacter(String name, int age, String gender, Location defaultLocation, Career career) {
        super(name, age, gender, defaultLocation);
        this.money = 1000.0;
        this.career = career;
        this.currentlyPlaying = true;
        initialiseNeeds();
        intialiseSkills();
    }

    private void initialiseNeeds() {
        needs.put("Hunger", new Hunger());
        needs.put("Hygiene", new Hygiene());
        needs.put("Energy", new Energy());
        needs.put("Fun", new Fun());
        needs.put("Social", new Social());
    }

    public void intialiseSkills() {
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
    // TODO: implement getters and setters for skills and needs

    public void currentlyPlaying(boolean state) {
        currentlyPlaying = state;
    }

    public void performActivity(Activity activity) {
        // System.out.println(name + " is doing " + activity.getName() + "...");

        // Map<String, Double> effects = activity.getNeedEffects();
        // for (Map.Entry<String, Double> entry : effects.entrySet()) {
        //     String needName = entry.getKey();
        //     Double amount = entry.getValue();

        //     if (needName.equals("Money")) {
        //         this.money += amount;
        //     } else if (needs.containsKey(needName)) {
        //         needs.get(needName).adjustNeed(amount);
        //     }
        // }

        // if (activity.getName().equals("Work")) {
        //     // Work improves Logic slightly?
        //     // Skills.get("Logic").practice(10.0);
        // } else if (activity.getName().equals("Socialize")) {
        //     // skills.get("Charisma").practice(15.0);
        // }
    }
}