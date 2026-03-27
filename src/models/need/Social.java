package models.need;

import models.character.SimCharacter;
import services.NotificationService;

/**
 * Social need implementation.
 */
public class Social extends Need {


    /**
     * Creates the social need with its default decay settings.
     */
    public Social() {
        super(NeedType.SOCIAL, 3.0);
    }

    /**
     * Applies the critical-low social penalty.
     *
     * @param sim the affected sim
     */
    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is feeling lonely! Try socializing with others soon! You have lost energy.");
        sim.adjustNeed(NeedType.ENERGY, -10);
    }

}
