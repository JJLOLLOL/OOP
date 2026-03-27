package models.need;

import models.character.SimCharacter;
import services.NotificationService;

/**
 * Energy need implementation.
 */
public class Energy extends Need {

    /**
     * Creates the energy need with its default decay settings.
     */
    public Energy() {
        super(NeedType.ENERGY, 8.0);
    }

    /**
     * Applies the critical-low energy penalty.
     *
     * @param sim the affected sim
     */
    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is exhausted! Find a place to rest soon! Needs will now decay faster.");
        sim.adjustNeed(NeedType.HUNGER, -5);
    }

}
