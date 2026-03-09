package models.needs;

import models.SimCharacter;

public class Hunger extends Need {

    public Hunger() {
        super("Hunger", 2.0); // Default decay rate for hunger
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is starving! Find food soon!");
        // Additional consequences can be implemented here (e.g., health decrease)
    }

}
