package models.needs;

import models.SimCharacter;

public class Hunger extends Need {

    private static final double DEFAULT_DECAYRATE = 2.0;

    public Hunger() {
        super("Hunger", DEFAULT_DECAYRATE);
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is starving! Find food soon!");
        // TODO: Implement unique behaviours when hunger is critically low
    }
}