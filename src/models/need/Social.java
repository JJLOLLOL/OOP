package models.need;

import models.character.SimCharacter;
import services.NotificationService;

public class Social extends Need {


    public Social() {
        super(NeedType.SOCIAL, 3.0);
    }

    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is feeling lonely! Try socializing with others soon! You have lost energy.");
        sim.adjustNeed(NeedType.ENERGY, -10);
    }

}