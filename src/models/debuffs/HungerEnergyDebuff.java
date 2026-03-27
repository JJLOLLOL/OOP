package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;


/**
 * Makes energy harder to recover and faster to lose when hunger is critical.
 */
public class HungerEnergyDebuff implements Debuff {

    private static final double RECOVERY_MULTIPLIER = 0.5;
    private static final double DECAY_MULTIPLIER = 2.0;

    /**
     * Activates when the sim's hunger need is critical.
     *
     * @param sim the sim being evaluated
     * @return {@code true} when the debuff should apply
     */
    @Override
    public boolean isActive(SimCharacter sim) {
        Need hunger = sim.getNeed(NeedType.HUNGER);
        if (hunger == null) {
            throw new IllegalArgumentException("hunger cannot be null.");
        }
        return hunger.isCritical();
    }

    /**
     * Reduces positive energy recovery while the debuff is active.
     *
     * @param sim the affected sim
     * @param type the need being modified
     * @param amount the original need delta
     * @return the adjusted need delta
     */
    @Override
    public double modifyNeedChange(SimCharacter sim, NeedType type, double amount) {
        if (type == NeedType.ENERGY && amount > 0) {
            return amount * RECOVERY_MULTIPLIER;
        }
        return amount;
    }

    /**
     * Doubles energy decay while the debuff is active.
     *
     * @param sim the affected sim
     * @param type the need being updated
     * @param baseDecay the unmodified decay rate
     * @return the adjusted decay rate
     */
    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        if (type == NeedType.ENERGY) {
            return baseDecay * DECAY_MULTIPLIER;
        }
        return baseDecay;
    }
}
