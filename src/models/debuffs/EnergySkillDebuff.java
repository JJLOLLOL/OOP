package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;
import models.skill.SkillType;

/**
 * Reduces skill gain when a sim's energy is critically low.
 */
public class EnergySkillDebuff implements Debuff {

    private static final double SKILL_GAIN_MULTIPLIER = 0.5;

    /**
     * Activates when the sim's energy need is critical.
     *
     * @param sim the sim being evaluated
     * @return {@code true} when the debuff should apply
     */
    @Override
    public boolean isActive(SimCharacter sim) {
        Need energy = sim.getNeed(NeedType.ENERGY);
        if (energy == null) {
            throw new IllegalArgumentException("Energy cannot be null.");
        }
        return energy.isCritical();
    }

    /**
     * Halves positive skill gains while the debuff is active.
     *
     * @param sim the affected sim
     * @param type the skill being modified
     * @param amount the original XP delta
     * @return the adjusted XP delta
     */
    @Override
    public double modifySkillChange(SimCharacter sim, SkillType type, double amount) {
        if (amount > 0) {
            return amount * SKILL_GAIN_MULTIPLIER;
        }
        return amount;
    }
}
