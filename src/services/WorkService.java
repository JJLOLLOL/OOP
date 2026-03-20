package services;

import Types.AchievementList;
import Types.CareerList;
import core.GameClock;
import java.util.List;
import java.util.Map;
import models.SimCharacter;
import models.actions.Furniture;
import models.actions.FurnitureAction;
import models.actions.FurnitureFactory;

public class WorkService {

    public static final int SHIFT_START_HOUR = 9;
    private static final double CAREER_XP_PER_SHIFT = 20.0;
    private static final double SKILL_XP_PER_HOUR = 5.0;

    private static final FurnitureAction WORK_ACTION
            = FurnitureFactory.createWorkDesk().getAction("Work");

    private static FurnitureAction loadWorkAction() {
        Furniture desk = FurnitureFactory.createWorkDesk();
        return desk.getAction("Work");
    }

    // ── Public entry point ────────────────────────────────────────────────────
    /**
     * Attempts to make the player work one shift.
     *
     * @param player the sim attempting to work
     * @param clock the current game clock (time will be advanced on success)
     * @param achievementService the achievement evaluator for unlock checks
     * @return a message describing the outcome, suitable for a notification
     */
    public static String work(
            SimCharacter player,
            GameClock clock,
            AchievementService achievementService) {
        if (player.getCareer().getCurrentCareer() == CareerList.JOBLESS) {
            return "You need a job before you can work!";
        }

        double fullShift = player.getCareer().getWorkingHours();
        int shiftEndH = SHIFT_START_HOUR + (int) fullShift;
        double currentTime = clock.getHours() + clock.getMinutes() / 60.0;

        if (currentTime < SHIFT_START_HOUR) {
            return String.format("Work doesn't start until %02d:00.", SHIFT_START_HOUR);
        }
        if (currentTime >= shiftEndH) {
            return String.format("The work day is over (shift ends %02d:00). Come back tomorrow!", shiftEndH);
        }

        double hoursWorked = shiftEndH - currentTime;
        double payFraction = hoursWorked / fullShift;
        boolean late = currentTime > SHIFT_START_HOUR;

        clock.advanceHours(hoursWorked);

        for (Map.Entry<String, Double> e : WORK_ACTION.affectedNeedsByActionMap().entrySet()) {
            NeedService.adjustNeed(player, e.getKey(), e.getValue() * payFraction);
        }

        double earned = player.getCareer().getSalary() * payFraction;
        player.setMoney(earned);

        String careerResult = player.getCareer().addProgress(CAREER_XP_PER_SHIFT * payFraction);
        if (careerResult.contains("Promoted")) {
            NotificationService.add(player, careerResult);
        }
        addAchievementNotifications(
                player,
                achievementService.evaluateCareerAchievements(player));

        for (String skill : player.getCareer().getCurrentCareer().getRelatedSkills()) {
            String result = NeedService.addSkillProgress(player, skill, SKILL_XP_PER_HOUR * hoursWorked);
            if (result != null && result.contains("levelled up")) {
                NotificationService.add(player, result);
            }
            addAchievementNotifications(
                    player,
                    achievementService.evaluateFirstTimeSkillAchievement(player, skill));
        }

        String timeMsg = late
                ? String.format("Arrived late. Worked %.1f / %.0f hours.", hoursWorked, fullShift)
                : String.format("Full shift complete (%.0f hours).", fullShift);
        return String.format("%s Earned $%.2f.", timeMsg, earned);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    /**
     * Applies the Work Desk action's need effects to the player, scaled by
     * {@code payFraction}. Uses the same need map defined in
     * {@link FurnitureFactory#createWorkDesk()} so work effects stay consistent
     * with all other activities.
     *
     * @param player the sim to affect
     * @param payFraction 1.0 for a full shift, less if the player arrived late
     */
    private static void applyNeedEffects(SimCharacter player, double payFraction) {
        for (Map.Entry<String, Double> effect : WORK_ACTION.affectedNeedsByActionMap().entrySet()) {
            NeedService.adjustNeed(player, effect.getKey(), effect.getValue() * payFraction);
        }
    }

    private static void addAchievementNotifications(
            SimCharacter player,
            List<AchievementList> unlockedAchievements) {
        for (AchievementList achievement : unlockedAchievements) {
            NotificationService.add(player, "Achievement unlocked: " + achievement.getTitle());
        }
    }
}
