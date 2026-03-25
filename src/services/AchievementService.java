package services;

import Types.AchievementList;
import Types.CareerList;
import Types.RelationshipList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import models.character.Character;
import models.character.SimCharacter;

/**
 * Manages the tracking and unlocking of achievements for Sims.
 * Handles different categories of achievements such as skills, careers, and social interactions.
 */
public class AchievementService {

    private static final Map<SimCharacter, Set<AchievementList>> unlocked
            = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<SimCharacter, Set<String>> firstTimeSkills
            = Collections.synchronizedMap(new WeakHashMap<>());

    // ── Access ────────────────────────────────────────────────────────────────
    public boolean unlockAchievement(SimCharacter sim, AchievementList achievement) {
        return unlocked.computeIfAbsent(sim, k -> new HashSet<>()).add(achievement);
    }

    public boolean hasAchievement(SimCharacter sim, AchievementList achievement) {
        Set<AchievementList> set = unlocked.get(sim);
        return set != null && set.contains(achievement);
    }

    public Set<AchievementList> getUnlockedAchievements(SimCharacter sim) {
        Set<AchievementList> set = unlocked.get(sim);
        return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
    }

    // ── Evaluators ────────────────────────────────────────────────────────────
    public List<AchievementList> evaluateFirstTimeSkillAchievement(SimCharacter sim, String skillName) {
        List<AchievementList> gained = new ArrayList<>();
        if (sim == null || skillName == null || skillName.isBlank()) {
            return gained;
        }

        String key = skillName.trim().toLowerCase();
        AchievementList achievement = skillAchievement(key);
        if (achievement == null) {
            return gained;
        }

        if (firstTimeSkills.computeIfAbsent(sim, k -> new HashSet<>()).add(key)
                && unlockAchievement(sim, achievement)) {
            gained.add(achievement);
        }
        return gained;
    }

    public List<AchievementList> evaluateCareerAchievements(SimCharacter sim) {
        List<AchievementList> gained = new ArrayList<>();
        if (sim == null) {
            return gained;
        }

        CareerList career = sim.getCareer().getCurrentCareer();
        if (career == CareerList.JOBLESS) {
            return gained;
        }

        tryUnlock(sim, AchievementList.FIRST_JOB, gained);

        AchievementList careerType = careerAchievement(career);
        if (careerType != null) {
            tryUnlock(sim, careerType, gained);
        }

        int rank = sim.getCareer().getCurrentRank();
        if (rank >= 2) {
            tryUnlock(sim, AchievementList.FIRST_PROMOTION, gained);
        }
        if (rank >= 4) {
            tryUnlock(sim, AchievementList.SENIOR_STAFF, gained);
        }
        if (rank >= 7) {
            tryUnlock(sim, AchievementList.CORPORATE_EXECUTIVE, gained);
        }

        return gained;
    }

    public List<AchievementList> evaluateWorkAchievements(SimCharacter sim) {
        List<AchievementList> gained = new ArrayList<>(evaluateCareerAchievements(sim));
        if (sim == null || sim.getCareer().getCurrentCareer() == CareerList.JOBLESS) {
            return gained;
        }

        for (String skill : sim.getCareer().getCurrentCareer().getRelatedSkills()) {
            gained.addAll(evaluateFirstTimeSkillAchievement(sim, skill));
        }
        return gained;
    }

    public List<AchievementList> evaluateSocialAchievements(
            SimCharacter sim,
            List<? extends Character> allCharacters,
            RelationshipService relationships) {

        List<AchievementList> gained = new ArrayList<>();
        boolean allFriends = true, allEnemies = true;

        for (Character other : allCharacters) {
            if (other == sim) {
                continue;
            }
            RelationshipList status = relationships.getStatus(sim, other);
            if (status != RelationshipList.FRIEND && status != RelationshipList.BEST_FRIEND) {
                allFriends = false;
            }
            if (status != RelationshipList.ENEMY) {
                allEnemies = false;
            }
            if (!allFriends && !allEnemies) {
                break;
            }
        }

        if (allFriends) {
            tryUnlock(sim, AchievementList.FRIENDLY, gained);
        }
        if (allEnemies) {
            tryUnlock(sim, AchievementList.EVIL, gained);
        }
        return gained;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void tryUnlock(SimCharacter sim, AchievementList type, List<AchievementList> gained) {
        if (unlockAchievement(sim, type)) {
            gained.add(type);
        }
    }

    private static AchievementList skillAchievement(String key) {
        return switch (key) {
            case "cooking" ->
                AchievementList.FIRST_COOKING;
            case "fitness" ->
                AchievementList.FIRST_FITNESS;
            case "programming" ->
                AchievementList.FIRST_PROGRAMMING;
            case "charisma" ->
                AchievementList.FIRST_CHARISMA;
            case "creativity" ->
                AchievementList.FIRST_CREATIVITY;
            case "logic" ->
                AchievementList.FIRST_LOGIC;
            case "gardening" ->
                AchievementList.FIRST_GARDENING;
            case "music" ->
                AchievementList.FIRST_MUSIC;
            case "writing" ->
                AchievementList.FIRST_WRITING;
            case "painting" ->
                AchievementList.FIRST_PAINTING;
            default ->
                null;
        };
    }

    private static AchievementList careerAchievement(CareerList career) {
        return switch (career) {
            case SOFTWARE_DEVELOPER, ENGINEER ->
                AchievementList.TECH_TRAILBLAZER;
            case DOCTOR ->
                AchievementList.HEALING_HANDS;
            case TEACHER, POLICE_OFFICER ->
                AchievementList.PUBLIC_SERVICE;
            case LAWYER, ACCOUNTANT, BUSINESS_MANAGER ->
                AchievementList.BUSINESS_MINDED;
            case ARTIST, MUSICIAN, WRITER ->
                AchievementList.CREATIVE_SOUL;
            default ->
                null;
        };
    }
}
