package models.need;

import models.character.SimCharacter;
import services.NotificationService;

/**
 * Hygiene need implementation.
 */
public class Hygiene extends Need {

    /**
     * Creates the hygiene need with its default decay settings.
     */
    public Hygiene() {
        super(NeedType.HYGIENE, 3.0);
    }

    /**
     * Applies the critical-low hygiene penalty.
     *
     * @param sim the affected sim
     */
    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is very dirty! Take a shower soon! Social need is decreased");
        sim.adjustNeed(NeedType.SOCIAL, -10);
    }

}
