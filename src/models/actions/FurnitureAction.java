package models.actions;

import core.GameClock;
import java.util.HashMap;
import java.util.Map;
import models.SimCharacter;
import models.needs.Need;
import services.NeedService;
import services.NotificationService;

/**
 * Represents a single action that can be performed on a {@link Furniture}.
 *
 * <p>
 * Each action defines need effects, skill effects, a money cost, and a time
 * cost ({@link #timeRequired}). The time cost is applied to the
 * {@link GameClock} when the overloaded
 * {@link #perform(SimCharacter, GameClock)} is used — the base
 * {@link #perform(SimCharacter)} skips time advancement for backwards
 * compatibility (e.g. automated/test calls).
 */
public class FurnitureAction implements ActivityInterface {

    private final String name;
    private final String description;
    private final Map<String, Double> affectedNeedsMap;
    private final Map<String, Double> affectedSkillsMap;
    private final double activityCost;
    private final double timeRequired; // in-game hours (e.g. 0.5 = 30 min, 8.0 = full sleep)

    /**
     * Constructs a new {@code FurnitureAction}.
     *
     * @param name the name of the action
     * @param description the description of the action
     * @param affectedNeedsMap a map of needs affected by the action and their
     * change values
     * @param affectedSkillsMap a map of skills affected by the action and their
     * change values
     * @param activityCost the monetary cost deducted when the action is
     * performed
     * @param timeRequired the time in in-game hours required to complete the
     * action
     */
    public FurnitureAction(
            String name,
            String description,
            Map<String, Double> affectedNeedsMap,
            Map<String, Double> affectedSkillsMap,
            double activityCost,
            double timeRequired) {
        this.name = name;
        this.description = description;
        this.affectedNeedsMap = new HashMap<>(affectedNeedsMap);
        this.affectedSkillsMap = new HashMap<>(affectedSkillsMap);
        this.activityCost = activityCost;
        this.timeRequired = timeRequired;
    }

    // ── ActivityInterface ─────────────────────────────────────────────────────
    @Override
    public Map<String, Double> affectedNeedsByActionMap() {
        return affectedNeedsMap;
    }

    @Override
    public Map<String, Double> affectedSkillsByActionMap() {
        return affectedSkillsMap;
    }

    @Override
    public double moneyDeducted() {
        return activityCost;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getTimeRequired() {
        return timeRequired;
    }

    /**
     * Performs the action and advances the in-game clock by
     * {@link #timeRequired} hours.
     *
     * @param character the sim performing the action (cannot be null)
     * @param clock the game clock to advance ({@code null} skips time
     * advancement)
     * @return {@code true} if the action succeeded; {@code false} if input
     * invalid or blocked
     * @throws IllegalArgumentException if character is null
     */
    @Override
    public boolean perform(SimCharacter character, GameClock clock) {
        if (character == null) {
            return false;
        }

        // ── Pre-checks ────────────────────────────────────────────────────────
        if (character.getMoney() < activityCost) {
            return false;
        }

        for (Map.Entry<String, Double> effect : affectedNeedsMap.entrySet()) {
            double amount = effect.getValue();
            if (amount >= 0) {
                continue;
            }
            Need need = character.getNeeds().get(effect.getKey());
            if (need == null) {
                continue;
            }
            if (need.getValue() < Math.abs(amount)) {
                return false;
            }
        }

        // ── Apply effects ─────────────────────────────────────────────────────
        character.setMoney(-activityCost);

        for (Map.Entry<String, Double> effect : affectedNeedsMap.entrySet()) {
            NeedService.adjustNeed(character, effect.getKey(), effect.getValue());
        }

        for (Map.Entry<String, Double> effect : affectedSkillsMap.entrySet()) {
            String result = NeedService.addSkillProgress(character, effect.getKey(), effect.getValue());
            if (result != null && result.contains("levelled up")) {
                NotificationService.add(character, result);
            }
        }

        // ── Advance time ──────────────────────────────────────────────────────
        if (clock != null && timeRequired > 0) {
            clock.advanceHours(timeRequired);
        }

        return true;
    }
}
