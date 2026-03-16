package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import models.furnitureactions.Furniture;
import models.needs.*;

public class SimCharacter extends Character {
    private boolean currentlyPlaying;
    private double money;
    private House house;
    private Map<String, Need> needs = new HashMap<>();
    private SkillsList skillsList = new SkillsList();
    private Career career;
    private Set<AchievementType> unlockedAchievements = new HashSet<>();
    private List<String> notifications = new ArrayList<>();

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


    //NOT FINALISED WORK METHOD, just added to test if the addProgress and updateSkills are working
    //please remove if needed during merge
    public String work() {
        String careerResult = career.addProgress(10.0);
        for (String skill : career.getCurrentCareer().getRelatedSkills()) {
            updateSkill(skill, 5.0);
        }
        return careerResult;
    }

    //skills methods
    public String updateSkill(String skillName, double amount) {
        Skills skill = skillsList.getSkill(skillName);
        if (skill == null) {
            return "Skill " + skillName + " not found!";
        }
        return skill.addProgress(amount);
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

    public boolean unlockAchievement(AchievementType achievement) {
        return unlockedAchievements.add(achievement);
    }

    public boolean hasAchievement(AchievementType achievement) {
        return unlockedAchievements.contains(achievement);
    }

    public Set<AchievementType> getUnlockedAchievements() {
        return Collections.unmodifiableSet(unlockedAchievements);
    }

    public void addNotification(String message) {
        notifications.add(message);
        if (notifications.size() > 3) {
            notifications.remove(0); // keep only last 3 to avoid panel UI overflow
        }
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void updateNeed(double deltaTime) {
        for (Need need : needs.values()) {
            need.decay(deltaTime);
            if (need.isCriticallyLow()) {
                if (!need.isCriticallyLowNotified()) {
                    need.onCriticallyLow(this);
                    need.setCriticallyLowNotified(true);
                }
            } else {
                need.setCriticallyLowNotified(false);
            }
        }
    }

}