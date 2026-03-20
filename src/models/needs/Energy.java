package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Energy extends Need {

    public Energy() {
        super("Energy", 1.5); // Default decay rate for energy
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is exhausted! Find a place to rest soon!");
    }

}
