package models.needs;

import models.SimCharacter;

/**
 * Represents the Social need of a Sim.
 * <p>
 * Social depletes over time. A critically low Social level causes loneliness,
 * which negatively impacts the Sim's Energy need.
 */
public class Social extends Need {

    /**
     * Default decay rate for Social need per game tick.
     */
    private static final double DEFAULT_DECAY_RATE = 3.0;

    /**
     * Constructs a {@code Social} need with its default decay rate.
     */
    public Social() {
        super("Social", DEFAULT_DECAY_RATE);
    }

    /**
     * Applies negative consequences when the Sim is lonely. Sends a loneliness
     * warning and decreases the Energy need.
     *
     * @param character the {@link SimCharacter} who is feeling lonely
     */
    @Override
    public CriticalConsequence getCriticalConsequences(SimCharacter character) {
        return new CriticalConsequence(
                character.getName() + " is feeling lonely! Try socializing with others soon! You have lost energy.",
                new CriticalConsequence.AffectedNeed("Energy", -10)
        );
    }

}
