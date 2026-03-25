package models.need;

import models.character.SimCharacter;
import services.NeedService;
import services.NotificationService;

/**
 * Represents the Hygiene need of a Sim.
 * <p>
 * Hygiene depletes over time. A critically low Hygiene level makes the Sim
 * dirty, which negatively impacts their Social need and interactions.
 */
public class Hygiene extends Need {

    /**
     * Constructs a {@code Hygiene} need with its default decay rate.
     */
    public Hygiene() {
        super("Hygiene", 3.0); // Default decay rate for hygiene
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
