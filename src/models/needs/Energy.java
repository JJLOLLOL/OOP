package models.needs;

import models.SimCharacter;

public class Energy extends Need {

    private static final double DEFAULT_DECAYRATE = 1.0;

    public Energy() {
        super("Energy", DEFAULT_DECAYRATE);
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is exhausted! Find a place to rest soon!");
        // TODO: Implement unique behaviours when energy is critically low
    }
}