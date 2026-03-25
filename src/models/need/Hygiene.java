package models.need;

import models.character.SimCharacter;
import services.NotificationService;

public class Hygiene extends Need {

    public Hygiene() {
        super(NeedType.HYGIENE, 3.0);
    }

    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is very dirty! Take a shower soon! Social need is decreased");
        sim.adjustNeed(NeedType.SOCIAL, -10);
    }

}