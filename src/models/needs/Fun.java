package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Fun extends Need {

    public Fun() {
        super("Fun", 1.0); // Default decay rate for fun
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is bored! Find something fun to do soon!");
    }

}
