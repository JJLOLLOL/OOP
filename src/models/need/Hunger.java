package models.need;

import models.character.SimCharacter;
import services.NotificationService;

/**
 * Represents the Hunger need of a Sim.
 * <p>
 * Hunger depletes over time. A critically low Hunger level causes starvation
 * and negatively impacts the Sim's energy levels.
 */
public class Hunger extends Need {

    /**
     * Constructs a {@code Hunger} need with its default decay rate.
     */
    public Hunger() {
        super("Hunger", 8.0); // Default decay rate for hunger
    }

    /**
     * Applies negative consequences when the Sim is starving.
     * Sends a starvation warning and triggers faster energy decay.
     *
     * @param character the {@link SimCharacter} who is starving
     */
    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is starving! Find food soon! Energy will decrease faster until hunger is restored.");
        // Note: Energy decay rate increase is handled by HungerEnergyDebuff.modifyNeedDecay()
        // which applies the multiplier through the debuff system in NeedService.updateNeeds()
    }

}
