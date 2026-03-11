package models.needs;

import models.SimCharacter;

public class Fun extends Need {

    public Fun() {
        super("Fun", 1.0); // Default decay rate for fun
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is bored! Find something fun to do soon!");
        // Additional consequences can be implemented here (e.g., mood decrease)
    }

}
