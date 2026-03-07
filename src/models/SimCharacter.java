package models;

import java.util.HashMap;
import java.util.Map;

public class SimCharacter extends Character {
    private boolean currentlyPlaying;
    private double money;
    private House house;
    private Map<String, Need> needs = new HashMap<>();
    private HashMap<String, Skills> skills = new HashMap<>();
    private HashMap<String, Career> careers = new HashMap<>();
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

    public void initialiseCareers() {
        careers.put("Software Developer", new Career("Software Developer", 4500.0, 8.0,  CareerTiers.INTERN));
        careers.put("Engineer",           new Career("Engineer",           4200.0, 8.0,  CareerTiers.INTERN));
        careers.put("Doctor",             new Career("Doctor",             6000.0, 10.0, CareerTiers.INTERN));
        careers.put("Teacher",            new Career("Teacher",            3500.0, 8.0,  CareerTiers.INTERN));
        careers.put("Lawyer",             new Career("Lawyer",             5500.0, 9.0,  CareerTiers.INTERN));
        careers.put("Police Officer",     new Career("Police Officer",     3200.0, 10.0, CareerTiers.INTERN));
        careers.put("Accountant",         new Career("Accountant",         3800.0, 8.0,  CareerTiers.INTERN));
        careers.put("Business Manager",   new Career("Business Manager",   5000.0, 9.0,  CareerTiers.INTERN));
        careers.put("Chef",               new Career("Chef",               3000.0, 10.0, CareerTiers.INTERN));
        careers.put("Artist",             new Career("Artist",             2500.0, 6.0,  CareerTiers.INTERN));
        careers.put("Musician",           new Career("Musician",           2600.0, 6.0,  CareerTiers.INTERN));
        careers.put("Writer",             new Career("Writer",             2800.0, 7.0,  CareerTiers.INTERN));
        careers.put("Jobless",            new Career("Jobless",            0.0,    0.0,  CareerTiers.INTERN));
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