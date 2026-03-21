package models.needs;

import models.SimCharacter;
import services.NotificationService;

public class Hunger extends Need {

    public Hunger() {
        super("Hunger", 10.0); // Default decay rate for hunger
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is starving! Find food soon! Energy will decrease faster until hunger is restored.");

        // Increase Energy's own decay rate when critically low (doubles the decay rate)
        Need energy = character.getNeeds().get("Energy");
        if (energy != null) {
            energy.setDecayRate(energy.getBaseDecayRate() * 2.0);
        }
    }

}
