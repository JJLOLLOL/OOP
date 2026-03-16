package models.needs;

import models.SimCharacter;

public class Hygiene extends Need {

    public Hygiene() {
        super("Hygiene", 1.0); // Default decay rate for hygiene
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        character.addNotification(character.getName() + " is very dirty! Take a shower soon!");
    }

}
