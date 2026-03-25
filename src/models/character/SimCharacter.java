package models.character;

import Types.CareerList;
import models.career.Career;
import models.location.House;
import models.location.Location;
import models.need.*;
import models.skill.Skill;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the main playable character in the game.
 * A Sim has money, a house, a career, a map of needs, and a map of skills.
 */
public class SimCharacter extends Character {

    private double money;
    private final Map<String, Need> needs = new HashMap<>();
    private final Map<String, Skill> skills = new HashMap<>();
    private Career career;
    private House currentHouse;

    /**
     * Constructs a new {@code SimCharacter}.
     * Initializes starting money, needs, skills, and sets the career to Jobless.
     *
     * @param name            the name of the Sim
     * @param age             the age of the Sim
     * @param gender          the gender of the Sim
     * @param defaultLocation the initial spawn location of the Sim
     */
    public SimCharacter(String name, int age, String gender, Location defaultLocation) {
        super(name, age, gender, defaultLocation);
        this.money = 1000.0;
        this.currentHouse = null;
        this.career = new Career(CareerList.JOBLESS);
        initialiseNeeds();
        initialiseSkills();
    }

    /**
     * Helper method to populate the Sim's initial needs.
     */
    private void initialiseNeeds() {
        needs.put("Hunger", new Hunger());
        needs.put("Hygiene", new Hygiene());
        needs.put("Energy", new Energy());
        needs.put("Fun", new Fun());
        needs.put("Social", new Social());
    }

    /**
     * Helper method to populate the Sim's initial skills.
     */
    private void initialiseSkills() {
        skills.put("Cooking", new Skill("Cooking"));
        skills.put("Fitness", new Skill("Fitness"));
        skills.put("Programming", new Skill("Programming"));
        skills.put("Charisma", new Skill("Charisma"));
        skills.put("Creativity", new Skill("Creativity"));
        skills.put("Logic", new Skill("Logic"));
        skills.put("Music", new Skill("Music"));
        skills.put("Writing", new Skill("Writing"));
        skills.put("Painting", new Skill("Painting"));
    }

    /**
     * Retrieves the Sim's current career object.
     *
     * @return the {@link Career}
     */
    public Career getCareer() {
        return career;
    }

    /**
     * Sets a new career for the Sim, replacing their old one.
     *
     * @param newCareer the new {@link CareerList} enum to join
     */
    public void joinCareer(CareerList newCareer) {
        this.career = new Career(newCareer);
    }

    /**
     * Retrieves the map of all skills the Sim possesses.
     *
     * @return a Map of skill names to {@link Skill} objects
     */
    public Map<String, Skill> getAllSkills() {
        return skills;
    }

    /**
     * Retrieves the map of all needs the Sim has.
     *
     * @return a Map of need names to {@link Need} objects
     */
    public Map<String, Need> getNeeds() {
        return needs;
    }

    /**
     * Adjusts the Sim's available money by a specified amount.
     *
     * @param amount the amount to add (can be negative to deduct)
     */
    public void setMoney(double amount) {
        money += amount;
    }

    /**
     * Retrieves the Sim's current money balance.
     *
     * @return the amount of money
     */
    public double getMoney() {
        return money;
    }

    /**
     * Retrieves the Sim's currently owned house.
     *
     * @return the {@link House} owned by the Sim
     */
    public House getCurrentHouse() {
        return currentHouse;
    }

    /**
     * Sets the house owned by the Sim.
     *
     * @param house the new {@link House}
     */
    public void setCurrentHouse(House house) {
        this.currentHouse = house;
    }
}
