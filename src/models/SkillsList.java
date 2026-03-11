package models;

import java.util.HashMap;

public class SkillsList {
    private HashMap<String, Skills> skills = new HashMap<>();
    public SkillsList(){
        initialiseSkills();
    }

    public void initialiseSkills() {
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

    //getter methods
    public Skills getSkill(String skillName) {
        return skills.get(skillName);
    }

    //returns all skills available, can be used to unlock activities
    public HashMap<String, Skills> getAllSkills() {
        return skills;
    }

    //display all skills and its XP progress
    public String displaySkills() {
        //StringBuilder allows for concatenation of strings more efficiently
        StringBuilder sb = new StringBuilder("=== Skills ===\n");
        for (Skills skill : skills.values()) {
            sb.append(skill.toString()).append("\n");
        }
        return sb.toString();
    }
}
