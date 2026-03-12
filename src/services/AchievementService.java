package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import models.AchievementType;
import models.Character;
import models.SimCharacter;

public class AchievementService {

    // Keep first-time skill tracking isolated in achievement layer.
    // WeakHashMap prevents retaining Sims forever if they are no longer referenced.
    private static final Map<SimCharacter, Set<String>> firstTimeSkillTracker =
            Collections.synchronizedMap(new WeakHashMap<>());

    public List<AchievementType> evaluateFirstTimeSkillAchievement(SimCharacter sim, String skillName) {
        List<AchievementType> newlyUnlocked = new ArrayList<>();
        if (sim == null || skillName == null || skillName.trim().isEmpty()) {
            return newlyUnlocked;
        }

        String normalizedSkill = skillName.trim().toLowerCase();
        AchievementType achievement = getFirstTimeSkillAchievement(normalizedSkill);
        if (achievement == null) {
            return newlyUnlocked;
        }

        if (!markSkillUsedFirstTime(sim, normalizedSkill)) {
            return newlyUnlocked;
        }

        if (sim.unlockAchievement(achievement)) {
            newlyUnlocked.add(achievement);
        }
        return newlyUnlocked;
    }

    private boolean markSkillUsedFirstTime(SimCharacter sim, String normalizedSkill) {
        Set<String> usedSkills = firstTimeSkillTracker.computeIfAbsent(sim, k -> new HashSet<>());
        return usedSkills.add(normalizedSkill);
    }

    public List<AchievementType> evaluateCareerAchievements(SimCharacter sim) {
        List<AchievementType> newlyUnlocked = new ArrayList<>();
        // Caller should invoke this only when the Sim has just obtained a non-jobless career.
        if (sim != null && sim.unlockAchievement(AchievementType.FIRST_JOB)) {
            newlyUnlocked.add(AchievementType.FIRST_JOB);
        }
        return newlyUnlocked;
    }

    public List<AchievementType> evaluateSocialAchievements(
            SimCharacter sim,
            List<? extends Character> allCharacters,
            RelationshipManager relationshipManager) {

        List<AchievementType> newlyUnlocked = new ArrayList<>();
        List<Character> others = getOtherCharacters(sim, allCharacters);

        if (others.isEmpty()) {
            return newlyUnlocked;
        }

        boolean allFriends = true;
        boolean allEnemies = true;

        for (Character other : others) {
            String status = relationshipManager.getStatus(sim, other);

            if (!"Friend".equals(status) && !"Best Friend".equals(status)) {
                allFriends = false;
            }

            if (!"Enemy".equals(status)) {
                allEnemies = false;
            }

            if (!allFriends && !allEnemies) {
                break;
            }
        }

        if (allFriends && sim.unlockAchievement(AchievementType.FRIENDLY)) {
            newlyUnlocked.add(AchievementType.FRIENDLY);
        }

        if (allEnemies && sim.unlockAchievement(AchievementType.EVIL)) {
            newlyUnlocked.add(AchievementType.EVIL);
        }

        return newlyUnlocked;
    }

    private List<Character> getOtherCharacters(SimCharacter sim, List<? extends Character> allCharacters) {
        List<Character> others = new ArrayList<>();
        for (Character character : allCharacters) {
            if (character != sim) {
                others.add(character);
            }
        }
        return others;
    }

    private AchievementType getFirstTimeSkillAchievement(String normalizedSkill) {
        switch (normalizedSkill) {
            case "cooking":
                return AchievementType.FIRST_COOKING;
            case "fitness":
                return AchievementType.FIRST_FITNESS;
            case "programming":
                return AchievementType.FIRST_PROGRAMMING;
            case "charisma":
                return AchievementType.FIRST_CHARISMA;
            case "creativity":
                return AchievementType.FIRST_CREATIVITY;
            case "logic":
                return AchievementType.FIRST_LOGIC;
            case "gardening":
                return AchievementType.FIRST_GARDENING;
            case "music":
                return AchievementType.FIRST_MUSIC;
            case "writing":
                return AchievementType.FIRST_WRITING;
            case "painting":
                return AchievementType.FIRST_PAINTING;
            default:
                return null;
        }
    }
}
