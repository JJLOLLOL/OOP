package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

public class SocialFunDebuff implements Debuff {

    private static final double FUN_DECAY_MULTIPLIER = 2.0;
    
    @Override
    public boolean isActive(SimCharacter sim) {
        Need social = sim.getNeed(NeedType.SOCIAL);
        if (social == null) {
            throw new IllegalArgumentException("Social cannot be null.");
        }
        return social.isCritical();

    }
    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        if (type == NeedType.FUN) {
            return baseDecay * FUN_DECAY_MULTIPLIER;
        }
        return baseDecay;
    }
}