package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Hunger extends Need {

    public Hunger() {
        super("Hunger", 2.0); // Default decay rate for hunger
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is starving! Find food soon!");
    }

}
