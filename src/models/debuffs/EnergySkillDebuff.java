package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;

/**
 * A debuff that reduces skill progression when the Energy need is critically low.
 * Simulates the difficulty of learning and focusing while exhausted.
 */
public class EnergySkillDebuff implements Debuff {
    @Override
    public double modifySkillChange(SimCharacter sim, String skillName, double amount) {
        // Debuff: Energy -> Skills (low energy -> slower skill gain)
        if (amount > 0) {
            Need energy = sim.getNeeds().get("Energy");
            if (energy != null && energy.isCritical()) {
                return amount * 0.5; // 50% slower skill progression
            }
        }
        return amount;
    }
}