package models.needs;

import models.SimCharacter;

public class Fun extends Need {

    private static final double DEFAULT_DECAYRATE = 1.0;

    public Fun() {
        super("Fun", DEFAULT_DECAYRATE);
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is bored! Find something fun to do soon!");
        // TODO: Implement unique behaviours when fun is critically low
    }
}