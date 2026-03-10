package models.needs;

import models.SimCharacter;

public class Hygiene extends Need {

    private static final double DEFAULT_DECAYRATE = 1.0;

    public Hygiene() {
        super("Hygiene", DEFAULT_DECAYRATE);
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is very dirty! Take a shower soon!");
        // TODO: Implement unique behaviours when hygiene is critically low
    }
}