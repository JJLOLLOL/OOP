package models.debuffs;

import java.util.ArrayList;
import java.util.List;

import models.action.ActionType;
import models.character.SimCharacter;
import models.need.NeedType;
import models.skill.SkillType;

/**
 * Holds and applies all active debuff rules in the game.
 * <p>
 * New debuff classes just need to be added to the {@code DEBUFFS} list to take effect globally.
 */
public class DebuffRegistry {
    private static final List<Debuff> DEBUFFS = new ArrayList<>();

    static {
        DEBUFFS.add(new HungerEnergyDebuff());
        DEBUFFS.add(new EnergySkillDebuff());
        DEBUFFS.add(new SocialFunDebuff());
        DEBUFFS.add(new HygieneSocialDebuff());
        DEBUFFS.add(new FatigueDecayDebuff());
    }

    /**
     * Applies all active debuff modifiers to a need change amount.
     *
     * @param sim      the {@link SimCharacter} experiencing the change
     * @param needName the name of the need being changed
     * @param amount   the original amount of change
     * @return the final modified amount after all debuffs have been applied
     */
    public static double applyNeedModifiers(SimCharacter sim, NeedType type, double amount) {
        double modifiedAmount = amount;
        for (Debuff debuff : DEBUFFS) {
            if (debuff.isActive(sim)) {
                modifiedAmount = debuff.modifyNeedChange(sim, type, modifiedAmount);
            }
        }
        return modifiedAmount;
    }

    /**
     * Applies all active debuff modifiers to a skill progress amount.
     *
     * @param sim       the {@link SimCharacter} gaining skill progress
     * @param skillName the name of the skill
     * @param amount    the original amount of progress
     * @return the final modified progress amount after all debuffs have been applied
     */
    public static double applySkillModifiers(SimCharacter sim, SkillType type, double amount) {
        double modifiedAmount = amount;
        for (Debuff debuff : DEBUFFS) {
            if (debuff.isActive(sim)) {
                modifiedAmount = debuff.modifySkillChange(sim, type, modifiedAmount);
            }
        }
        return modifiedAmount;
    }
    /**
     * Applies all active debuff modifiers to a need's decay rate.
     *
     * @param sim       the {@link SimCharacter} whose need is decaying
     * @param needName  the name of the need
     * @param baseDecay the original base decay rate
     * @return the final modified decay rate after all debuffs have been applied
     */
    public static double applyDecayModifiers(SimCharacter sim, NeedType type, double baseDecay) {
        double modifiedDecay = baseDecay;
        for (Debuff debuff : DEBUFFS) {
            if (debuff.isActive(sim)) {
                modifiedDecay = debuff.modifyNeedDecay(sim, type, modifiedDecay);
            }
        }
        return modifiedDecay;
    }

    /**
     * Checks if any active debuff blocks the specified interaction and returns the block reason.
     *
     * @param sim             the {@link SimCharacter} attempting the interaction
     * @param interactionType the type of interaction
     * @return the message explaining why the interaction is blocked, or {@code null} if it is allowed
     */

    public static String getInteractionBlockReason(SimCharacter sim, ActionType actionType) {
        for (Debuff debuff : DEBUFFS) {
            if (debuff.isActive(sim) && debuff.blockAction(sim, actionType)) {
                return debuff.getBlockMessage(sim);
            }
        }
        return null;
    }
}