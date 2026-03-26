package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * Implements fatigue effects: when Energy is critically low, other needs decay faster.
 * This simulates the real-world effect of exhaustion causing hunger, social withdrawal, etc.
 */
public class FatigueDecayDebuff implements Debuff {

    private static final double ENERGY_DECAY_MULTIPLIER = 2.0;
    private static final double OTHER_NEED_DECAY_BONUS = 1.0;


    @Override
    public boolean isActive(SimCharacter sim) {
        Need energy = sim.getNeed(NeedType.ENERGY);
        if (energy == null) {
            throw new IllegalArgumentException("Energy cannot be null.");
        }
        return energy.isCritical();
    }
    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        if (type == NeedType.ENERGY) {
            return baseDecay * ENERGY_DECAY_MULTIPLIER;
        }
        return baseDecay + OTHER_NEED_DECAY_BONUS;
    }

}