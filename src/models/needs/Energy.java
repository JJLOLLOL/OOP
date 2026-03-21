package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Energy extends Need {

    public Energy() {
        super("Energy", 7.5); // Default decay rate for energy
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is exhausted! Find a place to rest soon!");

        // Decrease Hunger when Energy is low (exhaustion makes you hungrier faster)
        Need hunger = character.getNeeds().get("Hunger");
        if (hunger != null) {
            hunger.adjustNeed(-5);
        }

        // Fatigue decay effects are handled via FatigueDecayDebuff.modifyNeedDecay()
    }

}
