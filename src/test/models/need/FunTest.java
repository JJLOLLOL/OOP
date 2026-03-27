package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import models.character.SimCharacter;
import models.location.Location;
import models.skill.SkillType;
import services.NotificationService;
import types.Gender;

class FunTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Fun fun = new Fun();

        assertEquals(NeedType.FUN, fun.getType());
        assertEquals("Fun", fun.getNeedName());
        assertEquals(80.0, fun.getValue());
        assertEquals(3.0, fun.getDecayRate());
        assertEquals(3.0, fun.getBaseDecayRate());
    }

    @Test
    void onCriticallyLow_addsNotificationAndReducesCharismaXp() {
        SimCharacter sim = new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", null));
        Fun fun = new Fun();

        double charismaXpBefore = sim.getStats().getSkillXp(SkillType.CHARISMA);

        fun.onCriticallyLow(sim);

        assertEquals(charismaXpBefore, sim.getStats().getSkillXp(SkillType.CHARISMA), 0.001);
        assertTrue(NotificationService.get(sim).stream()
                .anyMatch(msg -> msg.contains("Alex is bored! Find something fun to do soon!")));
    }
}