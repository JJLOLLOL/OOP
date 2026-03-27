package models.need;

import models.character.SimCharacter;
import services.NotificationService;

/**
 * Hunger need implementation.
 */
public class Hunger extends Need {

    /**
     * Creates the hunger need with its default decay settings.
     */
    public Hunger() {
        super(NeedType.HUNGER, 8.0);
    }

    /**
     * Applies the critical-low hunger penalty notification.
     *
     * @param character the affected sim
     */
    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is starving! Find food soon! Energy will decrease faster until hunger is restored.");
        // Note: Energy decay rate increase is handled by HungerEnergyDebuff.modifyNeedDecay()
        // which applies the multiplier through the debuff system in NeedService.updateNeeds()
    }

}
