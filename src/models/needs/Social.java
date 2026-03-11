package models.needs;

import models.SimCharacter;

public class Social extends Need {

    public Social() {
        super("Social", 1.0); // Default decay rate for social needs
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is feeling lonely! Try socializing with others soon!");
        // Additional consequences can be implemented here (e.g., mood decrease)
    }

}
