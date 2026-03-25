package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * A debuff that negatively impacts the Energy need when the Hunger need is critically low.
 * Reduces energy recovery from resting and increases the base decay rate of energy.
 */
public class HungerEnergyDebuff implements Debuff {
    @Override
    public double modifyNeedChange(SimCharacter sim, NeedType type, double amount) {
        // Debuff: Hunger -> Energy (low hunger -> poor sleep recovery)
        if (type == NeedType.ENERGY && amount > 0) {
            Need hunger = sim.getNeed(NeedType.HUNGER);
            if (hunger != null && hunger.isCritical()) {
                return amount * 0.5; // 50% recovery reduction
            }
        }
        return amount;
    }

    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        // Debuff: Hunger -> Energy decay (low hunger -> energy depletes faster)
        if (type == NeedType.ENERGY) {
            Need hunger = sim.getNeed(NeedType.HUNGER);
            if (hunger != null && hunger.isCritical()) {
                return baseDecay * 2.0; // Energy decays twice as fast
            }
        }
        return baseDecay;
    }
}