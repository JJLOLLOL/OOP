package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * Accelerates fun decay while the social need is critical.
 */
public class SocialFunDebuff implements Debuff {

    private static final double FUN_DECAY_MULTIPLIER = 2.0;

    /**
     * Activates when the sim's social need is critical.
     *
     * @param sim the sim being evaluated
     * @return {@code true} when the debuff should apply
     */
    @Override
    public boolean isActive(SimCharacter sim) {
        Need social = sim.getNeed(NeedType.SOCIAL);
        if (social == null) {
            throw new IllegalArgumentException("Social cannot be null.");
        }
        return social.isCritical();

    }

    /**
     * Doubles fun decay while the debuff is active.
     *
     * @param sim the affected sim
     * @param type the need being updated
     * @param baseDecay the unmodified decay rate
     * @return the adjusted decay rate
     */
    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        if (type == NeedType.FUN) {
            return baseDecay * FUN_DECAY_MULTIPLIER;
        }
        return baseDecay;
    }
}
