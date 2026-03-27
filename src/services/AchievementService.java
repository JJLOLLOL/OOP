package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import models.career.CareerList;
import models.character.Character;
import models.character.SimCharacter;
import models.skill.SkillType;
import types.AchievementType;
import types.RelationshipType;


public class AchievementService {

    private static final Map<SimCharacter, Set<AchievementType>> unlocked
            = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<SimCharacter, Set<SkillType>> firstTimeSkills
            = Collections.synchronizedMap(new WeakHashMap<>());

    // ── Access ────────────────────────────────────────────────────────────────
    public boolean unlockAchievement(SimCharacter sim, AchievementType achievement) {
        return unlocked.computeIfAbsent(sim, k -> new HashSet<>()).add(achievement);
    }

    public boolean hasAchievement(SimCharacter sim, AchievementType achievement) {
        Set<AchievementType> set = unlocked.get(sim);
        return set != null && set.contains(achievement);
    }

    public Set<AchievementType> getUnlockedAchievements(SimCharacter sim) {
        Set<AchievementType> set = unlocked.get(sim);
        return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
    }

    public List<AchievementType> evaluateFirstTimeSkillAchievement(SimCharacter sim, SkillType type) {
        List<AchievementType> gained = new ArrayList<>();
        if (sim == null || type == null) {
            return gained;
        }

        AchievementType achievement = skillAchievement(type);
        if (achievement == null) {
            return gained;
        }

        if (firstTimeSkills.computeIfAbsent(sim, k -> new HashSet<>()).add(type)
                && unlockAchievement(sim, achievement)) {
            gained.add(achievement);
        }
        return gained;
    }


    public List<AchievementType> evaluateCareerAchievements(SimCharacter sim) {
        List<AchievementType> gained = new ArrayList<>();
        if (sim == null) {
            return gained;
        }

        CareerList career = sim.getCareer().getCurrentCareer();
        if (career == CareerList.JOBLESS) {
            return gained;
        }

        tryUnlock(sim, AchievementType.FIRST_JOB, gained);

        AchievementType careerType = careerAchievement(career);
        if (careerType != null) {
            tryUnlock(sim, careerType, gained);
        }

        int rank = sim.getCareer().getCurrentRank();
        if (rank >= 2) {
            tryUnlock(sim, AchievementType.FIRST_PROMOTION, gained);
        }
        if (rank >= 4) {
            tryUnlock(sim, AchievementType.SENIOR_STAFF, gained);
        }
        if (rank >= 7) {
            tryUnlock(sim, AchievementType.CORPORATE_EXECUTIVE, gained);
        }

        return gained;
    }

    public List<AchievementType> evaluateWorkAchievements(SimCharacter sim) {
        List<AchievementType> gained = new ArrayList<>(evaluateCareerAchievements(sim));
        if (sim == null || sim.getCareer().getCurrentCareer() == CareerList.JOBLESS) {
            return gained;
        }

        for (SkillType skill : sim.getCareer().getCurrentCareer().getRelatedSkills()) {
            gained.addAll(evaluateFirstTimeSkillAchievement(sim, skill));
        }
        return gained;
    }

    public List<AchievementType> evaluateSocialAchievements(
            SimCharacter sim,
            List<? extends Character> allCharacters,
            RelationshipService relationships) {

        List<AchievementType> gained = new ArrayList<>();
        boolean allFriends = true, allEnemies = true;

        for (Character other : allCharacters) {
            if (other == sim) {
                continue;
            }
            RelationshipType status = sim.getRelationshipStatus(other);
            if (status != RelationshipType.FRIEND && status != RelationshipType.BEST_FRIEND) {
                allFriends = false;
            }
            if (status != RelationshipType.ENEMY) {
                allEnemies = false;
            }
            if (!allFriends && !allEnemies) {
                break;
            }
        }

        if (allFriends) {
            tryUnlock(sim, AchievementType.FRIENDLY, gained);
        }
        if (allEnemies) {
            tryUnlock(sim, AchievementType.EVIL, gained);
        }
        return gained;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void tryUnlock(SimCharacter sim, AchievementType type, List<AchievementType> gained) {
        if (unlockAchievement(sim, type)) {
            gained.add(type);
        }
    }

    private static AchievementType skillAchievement(SkillType type) {
        return switch (type) {
            case SkillType.COOKING ->
                AchievementType.FIRST_COOKING;
            case SkillType.FITNESS ->
                AchievementType.FIRST_FITNESS;
            case SkillType.PROGRAMMING ->
                AchievementType.FIRST_PROGRAMMING;
            case SkillType.CHARISMA ->
                AchievementType.FIRST_CHARISMA;
            case SkillType.CREATIVITY ->
                AchievementType.FIRST_CREATIVITY;
            case SkillType.LOGIC ->
                AchievementType.FIRST_LOGIC;
            case SkillType.MUSIC ->
                AchievementType.FIRST_MUSIC;
            case SkillType.WRITING ->
                AchievementType.FIRST_WRITING;
            case SkillType.PAINTING ->
                AchievementType.FIRST_PAINTING;
            default ->
                null;
        };
    }

    private static AchievementType careerAchievement(CareerList career) {
        return switch (career) {
            case SOFTWARE_DEVELOPER, ENGINEER ->
                AchievementType.TECH_TRAILBLAZER;
            case DOCTOR ->
                AchievementType.HEALING_HANDS;
            case TEACHER, POLICE_OFFICER ->
                AchievementType.PUBLIC_SERVICE;
            case LAWYER, ACCOUNTANT, BUSINESS_MANAGER ->
                AchievementType.BUSINESS_MINDED;
            case ARTIST, MUSICIAN, WRITER ->
                AchievementType.CREATIVE_SOUL;
            default ->
                null;
        };
    }
}
