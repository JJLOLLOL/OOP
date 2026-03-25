package models.debuffs;

import models.character.SimCharacter;

/**
 * Interface for the debuff system.
 * Implementing classes represent a single debuff rule that modifies game mechanics
 * when a specific condition (like a critically low need) is met.
 */
public interface Debuff {
    
    /**
     * Modifies the amount a Need is changing (e.g., reduces energy recovery).
     *
     * @param sim      the {@link SimCharacter} experiencing the change
     * @param needName the name of the need being changed
     * @param amount   the original amount of change
     * @return the modified amount of change
     */
    default double modifyNeedChange(SimCharacter sim, String needName, double amount) {
        return amount;
    }

    /**
     * Modifies the amount a Skill is progressing.
     *
     * @param sim       the {@link SimCharacter} gaining skill progress
     * @param skillName the name of the skill
     * @param amount    the original amount of progress
     * @return the modified amount of progress
     */
    default double modifySkillChange(SimCharacter sim, String skillName, double amount) {
        return amount;
    }

    /**
     * Modifies the decay rate of a Need.
     *
     * @param sim       the {@link SimCharacter} whose need is decaying
     * @param needName  the name of the need
     * @param baseDecay the original base decay rate
     * @return the modified decay rate
     */
    default double modifyNeedDecay(SimCharacter sim, String needName, double baseDecay) {
        return baseDecay;
    }

    /**
     * Checks if an interaction type is blocked by this debuff.
     *
     * @param sim             the {@link SimCharacter} attempting the interaction
     * @param interactionType the type of interaction being attempted
     * @return {@code true} if the interaction is blocked, {@code false} otherwise
     */
    default boolean blocksInteraction(SimCharacter sim, String interactionType) {
        return false;
    }

    /**
     * Returns the message to display when the interaction is blocked.
     *
     * @param sim the {@link SimCharacter} attempting the blocked interaction
     * @return the block message string
     */
    default String getBlockMessage(SimCharacter sim) {
        return "Action blocked due to a debuff.";
    }
}