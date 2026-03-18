package services;

import Types.AchievementType;
import Types.CareerList;
import Types.CareerRank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
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
        if (sim == null) {
            return newlyUnlocked;
        }

        CareerList careerType = resolveCareerType(sim);

        if (careerType == null || careerType == CareerList.JOBLESS) {
            return newlyUnlocked;
        }

        if (isFirstJob(sim, careerType) && sim.unlockAchievement(AchievementType.FIRST_JOB)) {
            newlyUnlocked.add(AchievementType.FIRST_JOB);
        }

        AchievementType careerAchievement = getCareerTypeAchievement(careerType);
        if (careerAchievement != null && sim.unlockAchievement(careerAchievement)) {
            newlyUnlocked.add(careerAchievement);
        }

        int rank = resolveCareerRank(sim);
        if (rank >= 2 && sim.unlockAchievement(AchievementType.FIRST_PROMOTION)) {
            newlyUnlocked.add(AchievementType.FIRST_PROMOTION);
        }
        if (rank >= 4 && sim.unlockAchievement(AchievementType.SENIOR_STAFF)) {
            newlyUnlocked.add(AchievementType.SENIOR_STAFF);
        }
        if (rank >= 7 && sim.unlockAchievement(AchievementType.CORPORATE_EXECUTIVE)) {
            newlyUnlocked.add(AchievementType.CORPORATE_EXECUTIVE);
        }
        return newlyUnlocked;
    }

    private boolean isFirstJob(SimCharacter sim, CareerList careerType) {
        return careerType != CareerList.JOBLESS && !sim.hasAchievement(AchievementType.FIRST_JOB);
    }

    private CareerList resolveCareerType(SimCharacter sim) {
        String careerInfo = sim.displayCareer();
        if (careerInfo == null || careerInfo.isBlank()) {
            return null;
        }

        for (CareerList career : CareerList.values()) {
            String titleLine = "Title:         " + career.getTitle();
            if (careerInfo.contains(titleLine)) {
                return career;
            }
        }
        return null;
    }

    private int resolveCareerRank(SimCharacter sim) {
        String careerInfo = sim.displayCareer();
        if (careerInfo == null || careerInfo.isBlank()) {
            return 0;
        }

        for (int rank = 1; rank <= CareerRank.RANK.length; rank++) {
            String rankLine = "Rank:          " + CareerRank.getTitle(rank);
            if (careerInfo.contains(rankLine)) {
                return rank;
            }
        }
        return 0;
    }

    public List<AchievementType> evaluateSocialAchievements(
            SimCharacter sim,
            List<? extends Character> allCharacters,
            RelationshipService relationshipManager) {

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

    private AchievementType getCareerTypeAchievement(CareerList careerType) {
        switch (careerType) {
            case SOFTWARE_DEVELOPER:
            case ENGINEER:
                return AchievementType.TECH_TRAILBLAZER;
            case DOCTOR:
                return AchievementType.HEALING_HANDS;
            case TEACHER:
            case POLICE_OFFICER:
                return AchievementType.PUBLIC_SERVICE;
            case LAWYER:
            case ACCOUNTANT:
            case BUSINESS_MANAGER:
                return AchievementType.BUSINESS_MINDED;
            case ARTIST:
            case MUSICIAN:
            case WRITER:
                return AchievementType.CREATIVE_SOUL;
            default:
                return null;
        }
    }
}
