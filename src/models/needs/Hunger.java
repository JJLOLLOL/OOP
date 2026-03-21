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
        // Note: Energy decay rate increase is handled by HungerEnergyDebuff.modifyNeedDecay()
        // which applies the multiplier through the debuff system in NeedService.updateNeeds()
    }

}
