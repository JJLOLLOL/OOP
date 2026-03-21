package models.needs;

import models.SimCharacter;
import services.NeedService;
import services.NotificationService;

public class Hygiene extends Need {

    public Hygiene() {
        super("Hygiene", 3.0); // Default decay rate for hygiene
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is very dirty! Take a shower soon! Social need is decreased");

        // Decrease Social when Hygiene is low (unpleasantness makes you less social)
        NeedService.adjustNeed(character, "Social", -10);
    }

}
