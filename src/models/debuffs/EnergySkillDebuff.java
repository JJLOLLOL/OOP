package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;
import models.skill.SkillType;

/**
 * A debuff that reduces skill progression when the Energy need is critically low.
 * Simulates the difficulty of learning and focusing while exhausted.
 */
public class EnergySkillDebuff implements Debuff {
    @Override
    public double modifySkillChange(SimCharacter sim, SkillType type, double amount) {
        // Debuff: Energy -> Skills (low energy -> slower skill gain)
        if (amount > 0) {
            Need energy = sim.getNeed(NeedType.ENERGY);
            if (energy != null && energy.isCritical()) {
                return amount * 0.5; // 50% slower skill progression
            }
        }
        return amount;
    }
}