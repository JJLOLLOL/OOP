package services;

import Types.CareerList;
import core.GameClock;
import models.SimCharacter;
import models.actions.Furniture;
import models.actions.FurnitureAction;
import models.actions.FurnitureFactory;

import java.util.Map;

/**
 * Handles the Work action for a {@link SimCharacter}.
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Validates the sim is employed and within working hours</li>
 * <li>Calculates late-arrival pay penalty</li>
 * <li>Delegates need effects to the Work Desk {@link FurnitureAction} (same
 * data source as all other activities in {@link FurnitureFactory})</li>
 * <li>Awards career XP and career-related skill XP</li>
 * <li>Advances the in-game clock by hours worked</li>
 * </ul>
 *
 * <h3>Late penalty</h3>
 * <pre>
 *   hoursWorked = shiftEnd − currentTime
 *   payFraction = hoursWorked / fullShift
 *   pay         = dailySalary × payFraction
 * </pre> All need effects and skill XP scale by the same {@code payFraction}.
 */
public class WorkService {

    /**
     * In-game hour at which every shift begins.
     */
    public static final int SHIFT_START_HOUR = 9;

    /**
     * Career XP awarded per full shift. Scales by payFraction for late
     * arrivals.
     */
    private static final double CAREER_XP_PER_SHIFT = 20.0;

    /**
     * Skill XP awarded per related skill per hour worked.
     */
    private static final double SKILL_XP_PER_HOUR = 5.0;

    /**
     * The Work Desk furniture template — its action defines the need/skill
     * effect maps for a full shift, consistent with all other activities.
     * Loaded once and reused.
     */
    private static final FurnitureAction WORK_ACTION = loadWorkAction();

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
     * @return a message describing the outcome, suitable for a notification
     */
    public static String work(SimCharacter player, GameClock clock) {
        if (player.getCareer().getCurrentCareer() == CareerList.JOBLESS) {
            return "You need a job before you can work!";
        }

        double fullShift = player.getCareer().getWorkingHours();
        int shiftEndH = SHIFT_START_HOUR + (int) fullShift;
        double currentTime = clock.getHours() + clock.getMinutes() / 60.0;

        // ── Working hours validation ───────────────────────────────────────────
        if (currentTime < SHIFT_START_HOUR) {
            return String.format("Work doesn't start until %02d:00.", SHIFT_START_HOUR);
        }
        if (currentTime >= shiftEndH) {
            return String.format("The work day is over (shift ends %02d:00). Come back tomorrow!", shiftEndH);
        }

        // ── Calculate hours worked and pay fraction ────────────────────────────
        double hoursWorked = shiftEndH - currentTime;
        double payFraction = hoursWorked / fullShift;
        boolean late = currentTime > SHIFT_START_HOUR;

        // ── Advance clock ─────────────────────────────────────────────────────
        clock.advanceHours(hoursWorked);

        // ── Apply need effects from FurnitureAction, scaled by payFraction ─────
        applyNeedEffects(player, payFraction);

        // ── Award pay ──────────────────────────────────────────────────────────
        double earned = player.getCareer().getSalary() * payFraction;
        player.setMoney(earned);

        // ── Career XP ─────────────────────────────────────────────────────────
        String careerResult = player.updateCareer(CAREER_XP_PER_SHIFT * payFraction);
        if (careerResult.contains("Promoted")) {
            player.addNotification(careerResult);
        }

        // ── Related skill XP ──────────────────────────────────────────────────
        for (String skillName : player.getCareer().getCurrentCareer().getRelatedSkills()) {
            String skillResult = player.addSkillProgress(skillName, SKILL_XP_PER_HOUR * hoursWorked);
            if (skillResult != null && skillResult.contains("levelled up")) {
                player.addNotification(skillResult);
            }
        }

        // ── Result message ─────────────────────────────────────────────────────
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
            player.adjustNeed(effect.getKey(), effect.getValue() * payFraction);
        }
    }
}
