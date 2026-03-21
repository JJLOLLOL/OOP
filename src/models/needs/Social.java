package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Social extends Need {

    public Social() {
        super("Social", 5.0); // Default decay rate for social needs
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is feeling lonely! Try socializing with others soon!");

        // Decrease Energy when Social is low (loneliness makes you less energetic)
        Need energy = character.getNeeds().get("Energy");
        if (energy != null) {
            energy.adjustNeed(-10);
        }
    }

}
