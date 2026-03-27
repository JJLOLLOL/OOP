package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import models.character.SimCharacter;
import models.location.Location;
import services.NotificationService;
import types.Gender;

class HygieneTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Hygiene hygiene = new Hygiene();

        assertEquals(NeedType.HYGIENE, hygiene.getType());
        assertEquals("Hygiene", hygiene.getNeedName());
        assertEquals(80.0, hygiene.getValue());
        assertEquals(3.0, hygiene.getDecayRate());
        assertEquals(3.0, hygiene.getBaseDecayRate());
    }

    @Test
    void onCriticallyLow_addsNotificationAndReducesSocial() {
        SimCharacter sim = new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", null));
        Hygiene hygiene = new Hygiene();

        double socialBefore = sim.getNeed(NeedType.SOCIAL).getValue();

        hygiene.onCriticallyLow(sim);

        assertEquals(socialBefore - 10.0, sim.getNeed(NeedType.SOCIAL).getValue(), 0.001);
        assertTrue(NotificationService.get(sim).stream()
                .anyMatch(msg -> msg.contains("Alex is very dirty! Take a shower soon!")));
    }
}