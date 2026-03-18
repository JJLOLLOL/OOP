package models.debuffs;

import models.SimCharacter;
import models.needs.Need;

public class EnergySkillDebuff implements Debuff {
    @Override
    public double modifySkillChange(SimCharacter sim, String skillName, double amount) {
        // Debuff: Energy -> Skills (low energy -> slower skill gain)
        if (amount > 0) {
            Need energy = sim.getNeeds().get("Energy");
            if (energy != null && energy.isCriticallyLow()) {
                return amount * 0.5; // 50% slower skill progression
            }
        }
        return amount;
    }
}