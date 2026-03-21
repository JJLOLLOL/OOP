package models.needs;

import models.SimCharacter;
import services.NeedService;
import services.NotificationService;

public class Fun extends Need {

    public Fun() {
        super("Fun", 5.0); // Default decay rate for fun
    }

    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is bored! Find something fun to do soon! Their charisma skill has suffered due to boredom.");

        // If fun is critically low, apply a one-time charisma penalty
        // Route through NeedService for centralized handling and debuff modifiers
        NeedService.addSkillProgress(character, "Charisma", -5);
    }

}
