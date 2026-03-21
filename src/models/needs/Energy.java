package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Energy extends Need {

    public Energy() {
        super("Energy", 1.5); // Default decay rate for energy
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is exhausted! Find a place to rest soon! Other needs will decay faster until energy is restored.");

        // If energy is critically low, increase the decay rate of other needs to simulate fatigue effects
        for (Need need : character.getNeeds().values()) {
            if (!need.getNeedName().equals("Energy")) {
                need.setDecayRate(need.getBaseDecayRate() + 1); // Increase decay of other needs by 1 when energy is critically low
            }
        }

        // Increase Energy's own decay rate when critically low (doubles the decay rate)
        this.setDecayRate(this.getBaseDecayRate() * 2.0);

        // Decrease Hunger when Energy is low (exhaustion makes you hungrier faster)
        Need hunger = character.getNeeds().get("Hunger");
        if (hunger != null) {
            hunger.adjustNeed(-5);
        }
    }

}
