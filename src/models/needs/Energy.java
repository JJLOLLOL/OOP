package models.needs;

import models.SimCharacter;

public class Energy extends Need {

    public Energy() {
        super("Energy", 1.5); // Default decay rate for energy
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        System.out.println(character.getName() + " is exhausted! Find a place to rest soon!");
        // Additional consequences can be implemented here (e.g., reduced performance in
        // activities)
    }

}
