package models.debuffs;

import java.util.ArrayList;
import java.util.List;
import models.SimCharacter;

/**
 * Holds and applies all active debuff rules in the game.
 * New debuff classes just need to be added to the DEBUFFS list to take effect globally.
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

    public static double applyNeedModifiers(SimCharacter sim, String needName, double amount) {
        double modifiedAmount = amount;
        for (Debuff debuff : DEBUFFS) {
            modifiedAmount = debuff.modifyNeedChange(sim, needName, modifiedAmount);
        }
        return modifiedAmount;
    }

    public static double applySkillModifiers(SimCharacter sim, String skillName, double amount) {
        double modifiedAmount = amount;
        for (Debuff debuff : DEBUFFS) {
            modifiedAmount = debuff.modifySkillChange(sim, skillName, modifiedAmount);
        }
        return modifiedAmount;
    }

    public static double applyDecayModifiers(SimCharacter sim, String needName, double baseDecay) {
        double modifiedDecay = baseDecay;
        for (Debuff debuff : DEBUFFS) {
            modifiedDecay = debuff.modifyNeedDecay(sim, needName, modifiedDecay);
        }
        return modifiedDecay;
    }

    public static String getInteractionBlockReason(SimCharacter sim, String interactionType) {
        for (Debuff debuff : DEBUFFS) {
            if (debuff.blocksInteraction(sim, interactionType)) {
                return debuff.getBlockMessage(sim);
            }
        }
        return null;
    }
}