package models.debuffs;

import models.action.ActionType;
import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * A debuff that blocks social interactions when the Hygiene need is critically low.
 * Simulates characters refusing to interact with a dirty Sim.
 */
public class HygieneSocialDebuff implements Debuff {
    @Override
    public boolean isActive(SimCharacter sim) {
        Need hygiene = sim.getNeed(NeedType.HYGIENE);
        if (hygiene == null) {
            throw new IllegalArgumentException("Hygiene cannot be null.");
        }
        return hygiene.isCritical();
    }
    @Override
    public boolean blockAction(SimCharacter sim, ActionType actionType) {
        return actionType == ActionType.SOCIALISE && isActive(sim);
    }

    @Override
    public String getBlockMessage(SimCharacter sim) {
        return "Your hygiene is too poor!";
    }
}