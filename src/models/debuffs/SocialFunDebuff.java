package models.debuffs;

import models.SimCharacter;
import models.needs.Need;

/**
 * A debuff that causes the Fun need to decay faster when the Social need is critically low.
 * Simulates the feeling of boredom and lack of enjoyment when lonely.
 */
public class SocialFunDebuff implements Debuff {
    @Override
    public double modifyNeedDecay(SimCharacter sim, String needName, double baseDecay) {
        // Debuff: Social -> Fun (low social -> fun decays faster)
        if ("Fun".equals(needName)) {
            Need social = sim.getNeeds().get("Social");
            if (social != null && social.isCriticallyLow()) {
                return baseDecay * 2.0; // Fun decays twice as fast
            }
        }
        return baseDecay;
    }
}