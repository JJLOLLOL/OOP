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


/**
 * Tracks unlocked achievements per sim and evaluates new unlocks from skill,
 * career, work, and social events.
 */
public class AchievementService {

    private static final Map<SimCharacter, Set<AchievementType>> unlocked
            = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<SimCharacter, Set<SkillType>> firstTimeSkills
            = Collections.synchronizedMap(new WeakHashMap<>());

    // ── Access ────────────────────────────────────────────────────────────────
    /**
     * Marks an achievement as unlocked for the given sim.
     *
     * @param sim the owning sim
     * @param achievement the achievement to unlock
     * @return {@code true} when the achievement was newly unlocked
     */
    public boolean unlockAchievement(SimCharacter sim, AchievementType achievement) {
        return unlocked.computeIfAbsent(sim, k -> new HashSet<>()).add(achievement);
    }

    /**
     * Checks whether the given sim has already unlocked the supplied
     * achievement.
     *
     * @param sim the sim to inspect
     * @param achievement the achievement being queried
     * @return {@code true} when the achievement is already unlocked
     */
    public boolean hasAchievement(SimCharacter sim, AchievementType achievement) {
        Set<AchievementType> set = unlocked.get(sim);
        return set != null && set.contains(achievement);
    }

    /**
     * Returns an unmodifiable view of the achievements unlocked by a sim.
     *
     * @param sim the sim to inspect
     * @return the unlocked achievements, or an empty set when none exist
     */
    public Set<AchievementType> getUnlockedAchievements(SimCharacter sim) {
        Set<AchievementType> set = unlocked.get(sim);
        return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
    }

    /**
     * Evaluates first-time skill usage achievements for a skill gain event.
     *
     * @param sim the sim whose skill changed
     * @param type the skill that was used
     * @return newly unlocked achievements, if any
     */
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


    /**
     * Evaluates career-start and promotion achievements for the supplied sim.
     *
     * @param sim the sim whose career state changed
     * @return newly unlocked achievements, if any
     */
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

    /**
     * Evaluates work-triggered achievements, including both career and
     * first-time related skill achievements.
     *
     * @param sim the sim who just worked
     * @return newly unlocked achievements, if any
     */
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

    /**
     * Evaluates social achievements that depend on the sim's relationship state
     * with every other character.
     *
     * @param sim the sim whose social state is being evaluated
     * @param allCharacters every character in the current game
     * @param relationships unused service reference kept for call-site symmetry
     * @return newly unlocked achievements, if any
     */
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
    /**
     * Attempts to unlock an achievement and records it in the accumulated
     * result list only when newly granted.
     */
    private void tryUnlock(SimCharacter sim, AchievementType type, List<AchievementType> gained) {
        if (unlockAchievement(sim, type)) {
            gained.add(type);
        }
    }

    /**
     * Maps a skill type to its corresponding first-use achievement.
     */
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

    /**
     * Maps a career choice to its category achievement, when one exists.
     */
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
