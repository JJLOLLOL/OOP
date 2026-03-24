package models.needs;

import models.SimCharacter;
import services.NeedService;
import services.NotificationService;

/**
 * Represents the Social need of a Sim.
 * <p>
 * Social depletes over time. A critically low Social level causes loneliness,
 * which negatively impacts the Sim's Energy need.
 */
public class Social extends Need {

    /** Default decay rate for Social need per game tick. */
    private static final double DEFAULT_DECAY_RATE = 3.0;

    /**
     * Constructs a {@code Social} need with its default decay rate.
     */
    public Social() {
        super("Social", DEFAULT_DECAY_RATE);
    }

    /**
     * Applies negative consequences when the Sim is lonely.
     * Sends a loneliness warning and decreases the Energy need.
     *
     * @param character the {@link SimCharacter} who is feeling lonely
     */
    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is feeling lonely! Try socializing with others soon! You have lost energy.");

        // Decrease Energy when Social is low (loneliness makes you less energetic)
        NeedService.adjustNeed(character, "Energy", -10);
    }

}
