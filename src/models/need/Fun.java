package models.need;

import models.character.SimCharacter;
import models.skill.SkillType;
import services.NotificationService;

public class Fun extends Need {
    public Fun() {
        super(NeedType.FUN, 3.0);
    }

    @Override
    public void onCriticallyLow(SimCharacter sim) {
        NotificationService.add(sim, sim.getName() + " is bored! Find something fun to do soon! Their charisma skill has suffered due to boredom.");
        sim.adjustSkillXp(SkillType.CHARISMA, -5);
    }

}