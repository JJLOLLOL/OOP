package models;

import java.util.HashMap;
import java.util.Map;
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
    private void initializeNeeds() {
        // Core gameplay mechanics: Needs
        registerNeed(new Hunger());
        registerNeed(new Energy());
        registerNeed(new Hygiene());
        registerNeed(new Fun());
        registerNeed(new Social());
    }

    private void registerNeed(Need need) {
        needs.put(need.getNeedName(), need);
    }

    public void performActivity(Activity activity) {
        System.out.println(name + " is doing " + activity.getName() + "...");

        Map<String, Double> effects = activity.getNeedEffects();
        for (Map.Entry<String, Double> entry : effects.entrySet()) {
            String needName = entry.getKey();
            Double amount = entry.getValue();

            if (needName.equals("Money")) {
                this.money += amount;
            } else if (needs.containsKey(needName)) {
                needs.get(needName).adjustNeed(amount);
            }
        }

        if (activity.getName().equals("Work")) {
            // Work improves Logic slightly?
            // Skills.get("Logic").practice(10.0);
        } else if (activity.getName().equals("Socialize")) {
            // skills.get("Charisma").practice(15.0);
        }
    }

    @Override
    public void update(int minutesPassed) {
    }
    @Override
    public void displayInfo() {
        System.out.println("=== " + name + " (" + agegroup + ") ===");
        System.out.println("Money: $" + String.format("%.2f", money));
        // System.out.println("Career: " + career);
        System.out.println("--- Needs ---");
        for (Need need : needs.values()) {
            System.out.println(need.toString());
        }
        // System.out.println("--- Skills ---");
        // for (Skill skill : skills.values()) {
        // System.out.println(skill.toString());
        // }
        System.out.println("==================");
    }

    public void performActivity(Activity activity) {
    }
}