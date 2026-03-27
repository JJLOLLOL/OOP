package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * Implements fatigue effects: when energy is critically low, energy drains even
 * faster and all other needs decay more quickly.
 */
public class FatigueDecayDebuff implements Debuff {

    private static final double ENERGY_DECAY_MULTIPLIER = 2.0;
    private static final double OTHER_NEED_DECAY_BONUS = 1.0;


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
     * Increases decay for every need while fatigue is active, with an extra
     * multiplier applied to energy itself.
     *
     * @param sim the affected sim
     * @param type the need being updated
     * @param baseDecay the unmodified decay rate
     * @return the adjusted decay rate
     */
    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        if (type == NeedType.ENERGY) {
            return baseDecay * ENERGY_DECAY_MULTIPLIER;
        }
        return baseDecay + OTHER_NEED_DECAY_BONUS;
    }

}
