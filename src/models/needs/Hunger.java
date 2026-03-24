package models.needs;

import models.SimCharacter;

/**
 * Represents the Hunger need of a Sim.
 * <p>
 * Hunger depletes over time. A critically low Hunger level causes starvation
 * and negatively impacts the Sim's energy levels.
 */
public class Hunger extends Need {

    /**
     * Default decay rate for Hunger need per game tick.
     */
    private static final double DEFAULT_DECAY_RATE = 8.0;

    /**
     * Constructs a {@code Hunger} need with its default decay rate.
     */
    public Hunger() {
        super("Hunger", DEFAULT_DECAY_RATE);
    }

    /**
     * Applies negative consequences when the Sim is starving. Sends a
     * starvation warning and triggers faster energy decay.
     *
     * @param character the {@link SimCharacter} who is starving
     */
    @Override
    public CriticalConsequence getCriticalConsequences(SimCharacter character) {
        return new CriticalConsequence(
                character.getName() + " is starving! Find food soon! Energy will decrease faster until hunger is restored.",
                new CriticalConsequence.AffectedNeed[] {
                        new CriticalConsequence.AffectedNeed("Energy", -10)
                }
        );
    }

}
