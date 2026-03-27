package models.need;

import models.character.SimCharacter;
import models.skill.SkillType;
import services.NotificationService;

/**
 * Fun need implementation.
 */
public class Fun extends Need {
    /**
     * Creates the fun need with its default decay settings.
     */
    public Fun() {
        super(NeedType.FUN, 3.0);
    }

    /**
     * Applies the critical-low fun penalty.
     *
     * @param sim the affected sim
     */
    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is bored! Find something fun to do soon! Their charisma skill has suffered due to boredom.");
        sim.adjustSkillXp(SkillType.CHARISMA, -5);
    }

}
