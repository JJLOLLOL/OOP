package models.debuffs;

import models.SimCharacter;

/**
 * Interface for the debuff system.
 * Implementing classes represent a single debuff rule that modifies game mechanics
 * when a specific condition (like a critically low need) is met.
 */
public interface Debuff {
    
    // Modifies the amount a Need is changing (e.g., reduces energy recovery)
    default double modifyNeedChange(SimCharacter sim, String needName, double amount) {
        return amount;
    }

    // Modifies the amount a Skill is progressing
    default double modifySkillChange(SimCharacter sim, String skillName, double amount) {
        return amount;
    }

    // Modifies the decay rate of a Need
    default double modifyNeedDecay(SimCharacter sim, String needName, double baseDecay) {
        return baseDecay;
    }

    // Checks if an interaction type is blocked by this debuff
    default boolean blocksInteraction(SimCharacter sim, String interactionType) {
        return false;
    }

    // Returns the message to display when the interaction is blocked
    default String getBlockMessage(SimCharacter sim) {
        return "Action blocked due to a debuff.";
    }
}