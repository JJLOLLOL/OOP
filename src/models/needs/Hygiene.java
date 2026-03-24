package models.needs;

import models.SimCharacter;
import services.NeedService;
import services.NotificationService;

/**
 * Represents the Hygiene need of a Sim.
 * <p>
 * Hygiene depletes over time. A critically low Hygiene level makes the Sim
 * dirty, which negatively impacts their Social need and interactions.
 */
public class Hygiene extends Need {

    /** Default decay rate for Hygiene need per game tick. */
    private static final double DEFAULT_DECAY_RATE = 3.0;

    /**
     * Constructs a {@code Hygiene} need with its default decay rate.
     */
    public Hygiene() {
        super("Hygiene", DEFAULT_DECAY_RATE);
    }

    /**
     * Applies negative consequences when the Sim is very dirty.
     * Sends a dirtiness warning and decreases the Social need.
     *
     * @param character the {@link SimCharacter} who is dirty
     */
    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is very dirty! Take a shower soon! Social need is decreased");

        // Decrease Social when Hygiene is low (unpleasantness makes you less social)
        NeedService.adjustNeed(character, "Social", -10);
    }

}
