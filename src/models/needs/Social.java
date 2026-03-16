package models.needs;

import models.SimCharacter;

public class Social extends Need {

    public Social() {
        super("Social", 1.0); // Default decay rate for social needs
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        character.addNotification(character.getName() + " is feeling lonely! Try socializing with others soon!");
    }

}
