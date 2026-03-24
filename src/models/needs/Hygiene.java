package models.needs;

import models.SimCharacter;

/**
 * Represents the Hygiene need of a Sim.
 * <p>
 * Hygiene depletes over time. A critically low Hygiene level makes the Sim
 * dirty, which negatively impacts their Social need and interactions.
 */
public class Hygiene extends Need {

    /**
     * Default decay rate for Hygiene need per game tick.
     */
    private static final double DEFAULT_DECAY_RATE = 3.0;

    /**
     * Constructs a {@code Hygiene} need with its default decay rate.
     */
    public Hygiene() {
        super("Hygiene", DEFAULT_DECAY_RATE);
    }

    /**
     * Applies negative consequences when the Sim is very dirty. Sends a
     * dirtiness warning and decreases the Social need.
     *
     * @param character the {@link SimCharacter} who is dirty
     */
    @Override
    public CriticalConsequence getCriticalConsequences(SimCharacter character) {
        return new CriticalConsequence(
                character.getName() + " is very dirty! Take a shower soon! Social need is decreased.",
                new CriticalConsequence.AffectedNeed("Social", -10)
        );
    }

}
