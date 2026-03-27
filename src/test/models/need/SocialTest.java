package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import models.character.SimCharacter;
import models.location.Location;
import services.NotificationService;
import types.Gender;

class SocialTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Social social = new Social();

        assertEquals(NeedType.SOCIAL, social.getType());
        assertEquals("Social", social.getNeedName());
        assertEquals(80.0, social.getValue());
        assertEquals(3.0, social.getDecayRate());
        assertEquals(3.0, social.getBaseDecayRate());
    }

    @Test
    void onCriticallyLow_addsNotificationAndReducesEnergy() {
        SimCharacter sim = new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", null));
        Social social = new Social();

        double energyBefore = sim.getNeed(NeedType.ENERGY).getValue();

        social.onCriticallyLow(sim);

        assertEquals(energyBefore - 10.0, sim.getNeed(NeedType.ENERGY).getValue(), 0.001);
        assertTrue(NotificationService.get(sim).stream()
                .anyMatch(msg -> msg.contains("Alex is feeling lonely! Try socializing with others soon!")));
    }
}