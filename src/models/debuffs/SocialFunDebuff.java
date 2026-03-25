package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * A debuff that causes the Fun need to decay faster when the Social need is critically low.
 * Simulates the feeling of boredom and lack of enjoyment when lonely.
 */
public class SocialFunDebuff implements Debuff {
    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        // Debuff: Social -> Fun (low social -> fun decays faster)
        if (type == NeedType.FUN) {
            Need social = sim.getNeed(NeedType.SOCIAL);
            if (social != null && social.isCritical()) {
                return baseDecay * 2.0; // Fun decays twice as fast
            }
        }
        return baseDecay;
    }
}