package models.needs;

import models.SimCharacter;

/**
 * Represents the Energy need of a Sim.
 * <p>
 * Energy depletes over time and through exertion. A critically low Energy level
 * causes exhaustion, which negatively impacts the Hunger need and overall need
 * decay.
 */
public class Energy extends Need {

    /**
     * Default decay rate for Energy need per game tick.
     */
    private static final double DEFAULT_DECAY_RATE = 5.5;

    /**
     * Constructs an {@code Energy} need with its default decay rate.
     */
    public Energy() {
        super("Energy", DEFAULT_DECAY_RATE);
    }

    /**
     * Applies negative consequences when the Sim is exhausted. Sends an
     * exhaustion warning and immediately decreases the Hunger need.
     * Additionally, triggers fatigue debuff effects elsewhere in the system.
     *
     * @param character the {@link SimCharacter} who is exhausted
     */
    @Override
    public CriticalConsequence getCriticalConsequences(SimCharacter character) {
        return new CriticalConsequence(
                character.getName() + " is exhausted! Hunger drops significantly (-10) and needs will now decay faster. Find a place to rest soon!",
                new CriticalConsequence.AffectedNeed("Hunger", -10)
        );
    }

}
