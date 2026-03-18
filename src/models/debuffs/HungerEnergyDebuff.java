package models.debuffs;

import models.SimCharacter;
import models.needs.Need;

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
}