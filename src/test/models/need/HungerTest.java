package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import models.character.SimCharacter;
import models.location.Location;
import services.NotificationService;
import types.Gender;

class HungerTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Hunger hunger = new Hunger();

        assertEquals(NeedType.HUNGER, hunger.getType());
        assertEquals("Hunger", hunger.getNeedName());
        assertEquals(80.0, hunger.getValue());
        assertEquals(8.0, hunger.getDecayRate());
        assertEquals(8.0, hunger.getBaseDecayRate());
    }

    @Test
    void onCriticallyLow_addsNotificationWithoutChangingNeeds() {
        SimCharacter sim = new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", null));
        Hunger hunger = new Hunger();

        double energyBefore = sim.getNeed(NeedType.ENERGY).getValue();
        double hungerBefore = sim.getNeed(NeedType.HUNGER).getValue();

        hunger.onCriticallyLow(sim);

        assertEquals(energyBefore, sim.getNeed(NeedType.ENERGY).getValue(), 0.001);
        assertEquals(hungerBefore, sim.getNeed(NeedType.HUNGER).getValue(), 0.001);
        assertTrue(NotificationService.get(sim).stream()
                .anyMatch(msg -> msg.contains("Alex is starving! Find food soon!")));
    }
}