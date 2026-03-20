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

    // Per-sim unlocked achievements — WeakHashMap so sims can be GC'd freely
    private static final Map<SimCharacter, Set<AchievementType>> unlockedAchievements
            = Collections.synchronizedMap(new WeakHashMap<>());

    // Per-sim first-time skill tracking
    private static final Map<SimCharacter, Set<String>> firstTimeSkillTracker
            = Collections.synchronizedMap(new WeakHashMap<>());

    // ── Achievement access ────────────────────────────────────────────────────
    public boolean unlockAchievement(SimCharacter sim, AchievementType achievement) {
        Set<AchievementType> unlocked = unlockedAchievements
                .computeIfAbsent(sim, k -> new HashSet<>());
        return unlocked.add(achievement);
    }

    public boolean hasAchievement(SimCharacter sim, AchievementType achievement) {
        Set<AchievementType> unlocked = unlockedAchievements.get(sim);
        return unlocked != null && unlocked.contains(achievement);
    }

    public Set<AchievementType> getUnlockedAchievements(SimCharacter sim) {
        Set<AchievementType> unlocked = unlockedAchievements.get(sim);
        return unlocked == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(unlocked);
    }

    // ── Evaluators ────────────────────────────────────────────────────────────
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

        Set<String> usedSkills = firstTimeSkillTracker.computeIfAbsent(sim, k -> new HashSet<>());
        if (!usedSkills.add(normalizedSkill)) {
            return newlyUnlocked;
        }

        if (unlockAchievement(sim, achievement)) {
            newlyUnlocked.add(achievement);
        }
        return newlyUnlocked;
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

        if (!hasAchievement(sim, AchievementType.FIRST_JOB)
                && unlockAchievement(sim, AchievementType.FIRST_JOB)) {
            newlyUnlocked.add(AchievementType.FIRST_JOB);
        }

        AchievementType careerAchievement = getCareerTypeAchievement(careerType);
        if (careerAchievement != null && unlockAchievement(sim, careerAchievement)) {
            newlyUnlocked.add(careerAchievement);
        }

        int rank = resolveCareerRank(sim);
        if (rank >= 2 && unlockAchievement(sim, AchievementType.FIRST_PROMOTION)) {
            newlyUnlocked.add(AchievementType.FIRST_PROMOTION);
        }
        if (rank >= 4 && unlockAchievement(sim, AchievementType.SENIOR_STAFF)) {
            newlyUnlocked.add(AchievementType.SENIOR_STAFF);
        }
        if (rank >= 7 && unlockAchievement(sim, AchievementType.CORPORATE_EXECUTIVE)) {
            newlyUnlocked.add(AchievementType.CORPORATE_EXECUTIVE);
        }
        return newlyUnlocked;
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

        if (allFriends && unlockAchievement(sim, AchievementType.FRIENDLY)) {
            newlyUnlocked.add(AchievementType.FRIENDLY);
        }
        if (allEnemies && unlockAchievement(sim, AchievementType.EVIL)) {
            newlyUnlocked.add(AchievementType.EVIL);
        }
        return newlyUnlocked;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private List<Character> getOtherCharacters(SimCharacter sim, List<? extends Character> all) {
        List<Character> others = new ArrayList<>();
        for (Character c : all) {
            if (c != sim) {
                others.add(c);
            }
        }
        return others;
    }

    private CareerList resolveCareerType(SimCharacter sim) {
        return sim.getCareer().getCurrentCareer();
    }

    private int resolveCareerRank(SimCharacter sim) {
        // Career.currentRank is 1-based; we read it via the rank title lookup
        String rankTitle = sim.getCareer().getRank();
        for (int rank = 1; rank <= CareerRank.RANK.length; rank++) {
            if (CareerRank.getTitle(rank).equals(rankTitle)) {
                return rank;
            }
        }
        return 0;
    }

    private AchievementType getFirstTimeSkillAchievement(String normalizedSkill) {
        return switch (normalizedSkill) {
            case "cooking" ->
                AchievementType.FIRST_COOKING;
            case "fitness" ->
                AchievementType.FIRST_FITNESS;
            case "programming" ->
                AchievementType.FIRST_PROGRAMMING;
            case "charisma" ->
                AchievementType.FIRST_CHARISMA;
            case "creativity" ->
                AchievementType.FIRST_CREATIVITY;
            case "logic" ->
                AchievementType.FIRST_LOGIC;
            case "gardening" ->
                AchievementType.FIRST_GARDENING;
            case "music" ->
                AchievementType.FIRST_MUSIC;
            case "writing" ->
                AchievementType.FIRST_WRITING;
            case "painting" ->
                AchievementType.FIRST_PAINTING;
            default ->
                null;
        };
    }

    private AchievementType getCareerTypeAchievement(CareerList careerType) {
        return switch (careerType) {
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
