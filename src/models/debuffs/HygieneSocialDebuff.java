package models.debuffs;

import models.SimCharacter;
import models.needs.Need;

public class HygieneSocialDebuff implements Debuff {
    @Override
    public boolean blocksInteraction(SimCharacter sim, String interactionType) {
        // Debuff: Hygiene -> NPC (low hygiene -> NPC rejects interaction)
        if ("Socialise".equals(interactionType)) {
            Need hygiene = sim.getNeeds().get("Hygiene");
            return hygiene != null && hygiene.isCriticallyLow();
        }
        return false;
    }

    @Override
    public String getBlockMessage(SimCharacter sim) {
        return "Your hygiene is too poor!";
    }
}