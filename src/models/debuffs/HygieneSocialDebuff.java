package models.debuffs;

import models.action.ActionType;
import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * Blocks social interactions when the hygiene need is critically low.
 */
public class HygieneSocialDebuff implements Debuff {

    /**
     * Activates when the sim's hygiene need is critical.
     *
     * @param sim the sim being evaluated
     * @return {@code true} when the debuff should apply
     */
    @Override
    public boolean isActive(SimCharacter sim) {
        Need hygiene = sim.getNeed(NeedType.HYGIENE);
        if (hygiene == null) {
            throw new IllegalArgumentException("Hygiene cannot be null.");
        }
        return hygiene.isCritical();
    }

    /**
     * Blocks social actions while the debuff is active.
     *
     * @param sim the affected sim
     * @param actionType the action being attempted
     * @return {@code true} when the action should be blocked
     */
    @Override
    public boolean blockAction(SimCharacter sim, ActionType actionType) {
        return actionType == ActionType.SOCIALISE && isActive(sim);
    }

    /**
     * Returns the player-facing reason that social interaction is blocked.
     *
     * @param sim the affected sim
     * @return the block message
     */
    @Override
    public String getBlockMessage(SimCharacter sim) {
        return "Your hygiene is too poor!";
    }
}
