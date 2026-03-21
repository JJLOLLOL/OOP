package models.needs;

import models.SimCharacter;
import services.NeedService;
import services.NotificationService;

public class Social extends Need {

    public Social() {
        super("Social", 3.0); // Default decay rate for social needs
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is feeling lonely! Try socializing with others soon! You have lost energy.");

        // Decrease Energy when Social is low (loneliness makes you less energetic)
        NeedService.adjustNeed(character, "Energy", -10);
    }

}
