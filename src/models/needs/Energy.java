package models.needs;

import models.SimCharacter;
import services.NeedService;
import services.NotificationService;

/**
 * Represents the Energy need of a Sim.
 * <p>
 * Energy depletes over time and through exertion. A critically low Energy level
 * causes exhaustion, which negatively impacts the Hunger need and overall need decay.
 */
public class Energy extends Need {

    /**
     * Constructs an {@code Energy} need with its default decay rate.
     */
    public Energy() {
        super("Energy", 5.5); // Default decay rate for energy
    }

    /**
     * Applies negative consequences when the Sim is exhausted.
     * Sends an exhaustion warning and immediately decreases the Hunger need.
     * Additionally, triggers fatigue debuff effects elsewhere in the system.
     *
     * @param character the {@link SimCharacter} who is exhausted
     */
    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is exhausted! Find a place to rest soon! Needs will now decay faster.");

        // Decrease Hunger when Energy is low (exhaustion makes you hungrier faster)
        NeedService.adjustNeed(character, "Hunger", -5);

        // Fatigue decay effects are handled via FatigueDecayDebuff.modifyNeedDecay()
    }

}
