package models.debuffs;

import models.action.ActionType;
import models.character.SimCharacter;

import models.need.NeedType;
import models.skill.SkillType;

/**
 * Contract for conditional gameplay modifiers that alter needs, skills, decay,
 * or action availability.
 */
public interface Debuff {
    /**
     * Returns whether this debuff currently applies to the supplied sim.
     *
     * @param sim the sim being evaluated
     * @return {@code true} when the debuff is active
     */
    boolean isActive(SimCharacter sim);

    /**
     * Modifies a need delta while the debuff is active.
     */
    default double modifyNeedChange(SimCharacter sim, NeedType type, double amount) {
        return amount;
    }

    /**
     * Modifies a skill XP delta while the debuff is active.
     */
    default double modifySkillChange(SimCharacter sim, SkillType type, double amount) {
        return amount;
    }

    /**
     * Modifies a need decay rate while the debuff is active.
     */
    default double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        return baseDecay;
    }

    /**
     * Returns whether the debuff blocks the supplied action type.
     */
    default boolean blockAction(SimCharacter sim, ActionType actionType) {
        return false;
    }

    /**
     * Returns the player-facing message for a blocked action.
     */
    default String getBlockMessage(SimCharacter sim) {
        return "Action blocked due to a debuff.";
    }
}
