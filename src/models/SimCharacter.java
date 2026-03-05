package models;

import java.util.HashMap;
import java.util.Map;

public class SimCharacter extends Character {
    private boolean currentlyPlaying;
    private double money;
    private House house;
    private Map<String, Need> needs = new HashMap<>();
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

    // getters & setters
    public void setMoney(double amount) {
        money += amount;
    }
    public double getMoney() {
        return money;
    }

    @Override
    public void update(int minutesPassed) {
    }
    @Override
    public void displayInfo() {
    }

    public void performActivity(Activity activity) {
    }
}