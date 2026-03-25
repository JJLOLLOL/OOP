package models.need;

import models.character.SimCharacter;
import services.NotificationService;

public class Energy extends Need {

    public Energy() {
        super(NeedType.ENERGY, 8.0);
    }

    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is exhausted! Find a place to rest soon! Needs will now decay faster.");
        sim.adjustNeed(NeedType.HUNGER, -5);
    }

}