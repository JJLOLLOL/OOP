package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;


public class HungerEnergyDebuff implements Debuff {

    private static final double RECOVERY_MULTIPLIER = 0.5;
    private static final double DECAY_MULTIPLIER = 2.0;


    @Override
    public boolean isActive(SimCharacter sim) {
        Need hunger = sim.getNeed(NeedType.HUNGER);
        if (hunger == null) {
            throw new IllegalArgumentException("hunger cannot be null.");
        }
        return hunger.isCritical();
    }

    @Override
    public double modifyNeedChange(SimCharacter sim, NeedType type, double amount) {
        if (type == NeedType.ENERGY && amount > 0) {
            return amount * RECOVERY_MULTIPLIER;
        }
        return amount;
    }
    
    @Override
    public double modifyNeedDecay(SimCharacter sim, NeedType type, double baseDecay) {
        if (type == NeedType.ENERGY) {
            return baseDecay * DECAY_MULTIPLIER;
        }
        return baseDecay;
    }
}