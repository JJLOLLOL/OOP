package models.needs;

import models.SimCharacter;

public class Hunger extends Need {

    public Hunger() {
        super("Hunger", 2.0); // Default decay rate for hunger
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        character.addNotification(character.getName() + " is starving! Find food soon!");
    }

}
