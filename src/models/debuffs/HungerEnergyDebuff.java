package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;

/**
 * A debuff that negatively impacts the Energy need when the Hunger need is critically low.
 * Reduces energy recovery from resting and increases the base decay rate of energy.
 */
public class HungerEnergyDebuff implements Debuff {
    @Override
    public double modifyNeedChange(SimCharacter sim, String needName, double amount) {
        // Debuff: Hunger -> Energy (low hunger -> poor sleep recovery)
        if ("Energy".equals(needName) && amount > 0) {
            Need hunger = sim.getNeeds().get("Hunger");
            if (hunger != null && hunger.isCriticallyLow()) {
                return amount * 0.5; // 50% recovery reduction
            }
        }
        return amount;
    }

    @Override
    public double modifyNeedDecay(SimCharacter sim, String needName, double baseDecay) {
        // Debuff: Hunger -> Energy decay (low hunger -> energy depletes faster)
        if ("Energy".equals(needName)) {
            Need hunger = sim.getNeeds().get("Hunger");
            if (hunger != null && hunger.isCriticallyLow()) {
                return baseDecay * 2.0; // Energy decays twice as fast
            }
        }
        return baseDecay;
    }
}