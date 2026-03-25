package models.need;

import models.character.SimCharacter;
import services.NeedService;
import services.NotificationService;

/**
 * Represents the Fun need of a Sim.
 * <p>
 * Fun depletes over time. A critically low Fun level causes boredom,
 * which negatively impacts the Sim's Charisma skill progress.
 */
public class Fun extends Need {

    /**
     * Constructs a {@code Fun} need with its default decay rate.
     */
    public Fun() {
        super("Fun", 3.0); // Default decay rate for fun
    }

    /**
     * Applies negative consequences when the Sim is bored.
     * Sends a boredom warning and applies a penalty to the Charisma skill.
     *
     * @param character the {@link SimCharacter} who is bored
     */
    @Override
    public void onCriticallyLow(SimCharacter character) {
        NotificationService.add(character, character.getName() + " is bored! Find something fun to do soon! Their charisma skill has suffered due to boredom.");

        // If fun is critically low, apply a one-time charisma penalty
        // Route through NeedService for centralized handling and debuff modifiers
        NeedService.addSkillProgress(character, "Charisma", -5);
    }

}
