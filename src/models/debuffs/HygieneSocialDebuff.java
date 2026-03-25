package models.debuffs;

import models.character.SimCharacter;
import models.need.Need;
import models.need.NeedType;

/**
 * A debuff that blocks social interactions when the Hygiene need is critically low.
 * Simulates characters refusing to interact with a dirty Sim.
 */
public class HygieneSocialDebuff implements Debuff {
    @Override
    public boolean blocksInteraction(SimCharacter sim, String interactionType) {
        // Debuff: Hygiene -> NPC (low hygiene -> NPC rejects interaction)
        if ("Socialise".equals(interactionType)) {
            Need hygiene = sim.getNeed(NeedType.HYGIENE);
            return hygiene != null && hygiene.isCritical();
        }
        return false;
    }

    @Override
    public String getBlockMessage(SimCharacter sim) {
        return "Your hygiene is too poor!";
    }
}