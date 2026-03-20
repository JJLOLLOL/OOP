package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Hygiene extends Need {

    public Hygiene() {
        super("Hygiene", 1.0); // Default decay rate for hygiene
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is very dirty! Take a shower soon!");
    }

}
