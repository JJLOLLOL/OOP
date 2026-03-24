package models.needs;

import models.SimCharacter;

/**
 * Represents the Fun need of a Sim.
 * <p>
 * Fun depletes over time. A critically low Fun level causes boredom, which
 * negatively impacts the Sim's Charisma skill progress.
 */
public class Fun extends Need {

    /**
     * Default decay rate for Fun need per game tick.
     */
    private static final double DEFAULT_DECAY_RATE = 3.0;

    /**
     * Constructs a {@code Fun} need with its default decay rate.
     */
    public Fun() {
        super("Fun", DEFAULT_DECAY_RATE);
    }

    /**
     * Applies negative consequences when the Sim is bored. Sends a boredom
     * warning and applies a penalty to the Charisma skill.
     *
     * @param character the {@link SimCharacter} who is bored
     */
    @Override
    public CriticalConsequence getCriticalConsequences(SimCharacter character) {
        return new CriticalConsequence(
                character.getName() + " is bored! Find something fun to do soon! Their charisma skill has suffered due to boredom.",
                null,
                new CriticalConsequence.AffectedSkill[]{
                    new CriticalConsequence.AffectedSkill("Charisma", -5)
                }
        );
    }

}
