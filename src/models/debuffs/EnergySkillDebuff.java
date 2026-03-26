package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;
import models.skill.SkillType;

public class EnergySkillDebuff implements Debuff {

    private static final double SKILL_GAIN_MULTIPLIER = 0.5;

    @Override
    public boolean isActive(SimCharacter sim) {
        Need energy = sim.getNeed(NeedType.ENERGY);
        if (energy == null) {
            throw new IllegalArgumentException("Energy cannot be null.");
        }
        return energy.isCritical();
    }
    
    @Override
    public double modifySkillChange(SimCharacter sim, SkillType type, double amount) {
        if (amount > 0) {
            return amount * SKILL_GAIN_MULTIPLIER;
        }
        return amount;
    }
}