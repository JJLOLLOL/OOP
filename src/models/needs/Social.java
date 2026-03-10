package models.needs;

import models.SimCharacter;

public class Social extends Need {

    private static final double DEFAULT_DECAYRATE = 1.0;

    public Social() {
        super("Social", DEFAULT_DECAYRATE);
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is feeling lonely! Try socializing with others soon!");
        // TODO: Implement unique behaviours when hygiene is critically low
    }
}