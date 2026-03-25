package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;

/**
 * Implements fatigue effects: when Energy is critically low, other needs decay faster.
 * This simulates the real-world effect of exhaustion causing hunger, social withdrawal, etc.
 */
public class FatigueDecayDebuff implements Debuff {
    @Override
    public double modifyNeedDecay(SimCharacter sim, String needName, double baseDecay) {
        Need energy = sim.getNeeds().get("Energy");
        if (energy != null && energy.isCriticallyLow()) {
            // Energy is critically low: increase decay of other needs
            if (!needName.equals("Energy")) {
                return baseDecay + 1; // All other needs decay 1 point faster per second
            }
            // Energy itself decays twice as fast when critically low
            return baseDecay * 2.0;
        }
        return baseDecay;
    }
}
