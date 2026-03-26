package services;

import Types.CareerList;
import core.GameClock;
import java.util.Map;

import models.actions.FurnitureAction;
import models.actions.FurnitureFactory;
import models.character.SimCharacter;
import models.need.NeedType;
import models.skill.SkillType;

public class WorkService {

    public static final int SHIFT_START_HOUR = 9;
    private static final double CAREER_XP_PER_SHIFT = 20.0;
    private static final double SKILL_XP_PER_HOUR = 5.0;

    private static final FurnitureAction WORK_ACTION
            = FurnitureFactory.createWorkDesk().getAction("Work");

    // ── Public entry point ────────────────────────────────────────────────────
    /**
     * Attempts to make the player work one shift.
     *
     * @param player the sim attempting to work
     * @param clock the current game clock (time will be advanced on success)
     * @return a message describing the outcome, suitable for a notification
     */
    public static String work(SimCharacter player, GameClock clock) {
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
            player.adjustNeed(NeedType.getType(e.getKey()), e.getValue() * payFraction);
        }

        double earned = player.getCareer().getSalary() * payFraction;
        player.earnMoney(earned);

        String careerResult = player.getCareer().addProgress(CAREER_XP_PER_SHIFT * payFraction);
        if (careerResult.contains("Promoted")) {
            NotificationService.add(player, careerResult);
        }

        for (String skill : player.getCareer().getCurrentCareer().getRelatedSkills()) {
            int levelUpCount = player.adjustSkillXp(SkillType.getType(skill), SKILL_XP_PER_HOUR * hoursWorked);
            if (levelUpCount > 0) {
                NotificationService.add(player, "Levelled Up by " + levelUpCount);
            }
        }

        String timeMsg = late
                ? String.format("Arrived late. Worked %.1f / %.0f hours.", hoursWorked, fullShift)
                : String.format("Full shift complete (%.0f hours).", fullShift);
        return String.format("%s Earned $%.2f.", timeMsg, earned);
    }
}
