package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import models.character.SimCharacter;
import models.location.Location;
import services.NotificationService;
import types.Gender;

class EnergyTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Energy energy = new Energy();

        assertEquals(NeedType.ENERGY, energy.getType());
        assertEquals("Energy", energy.getNeedName());
        assertEquals(80.0, energy.getValue());
        assertEquals(8.0, energy.getDecayRate());
        assertEquals(8.0, energy.getBaseDecayRate());
    }

    @Test
    void onCriticallyLow_addsNotificationAndReducesHunger() {
        SimCharacter sim = new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", null));
        Energy energy = new Energy();

        double hungerBefore = sim.getNeed(NeedType.HUNGER).getValue();

        energy.onCriticallyLow(sim);

        assertEquals(hungerBefore - 5.0, sim.getNeed(NeedType.HUNGER).getValue(), 0.001);
        assertTrue(NotificationService.get(sim).stream()
                .anyMatch(msg -> msg.contains("Alex is exhausted! Find a place to rest soon!")));
    }
}