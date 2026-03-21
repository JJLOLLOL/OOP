package models.needs;

import models.SimCharacter;
import services.NeedService;
import services.NotificationService;

public class Energy extends Need {

    public Energy() {
        super("Energy", 5.5); // Default decay rate for energy
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is exhausted! Find a place to rest soon! Needs will now decay faster.");

        // Decrease Hunger when Energy is low (exhaustion makes you hungrier faster)
        NeedService.adjustNeed(character, "Hunger", -5);

        // Fatigue decay effects are handled via FatigueDecayDebuff.modifyNeedDecay()
    }

}
