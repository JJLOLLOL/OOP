package models.debuffs;

import models.character.SimCharacter;

import models.need.NeedType;
import models.skill.SkillType;

public interface Debuff {
    default double modifyNeedChange(SimCharacter sim, NeedType type, double amount) {
        return amount;
    }
    default double modifySkillChange(SimCharacter sim, SkillType type, double amount) {
        return amount;
    }
    default double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        return baseDecay;
    }
    default boolean blocksInteraction(SimCharacter sim, String interactionType) {
        return false;
    }
    default String getBlockMessage(SimCharacter sim) {
        return "Action blocked due to a debuff.";
    }
}