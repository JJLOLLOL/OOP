package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import models.character.SimCharacter;
import models.location.Location;
import org.junit.jupiter.api.Test;

class NeedTest {

    private static class TestNeed extends Need {
        TestNeed(NeedType type, double decayRate) {
            super(type, decayRate);
        }

        @Override
        public void onCriticallyLow(SimCharacter character) {
        }
    }

    private SimCharacter createSim() {
        return new SimCharacter("Jamie", 22, "Non-binary", new Location("Home", new ArrayList<>()));
    }

    @Test
    void constructorRejectsInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> new TestNeed(null, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new TestNeed(NeedType.HUNGER, -1.0));
    }

    @Test
    void constructorInitializesDefaults() {
        Need need = new TestNeed(NeedType.HUNGER, 4.0);

        assertEquals(NeedType.HUNGER, need.getType());
        assertEquals("Hunger", need.getNeedName());
        assertEquals(80.0, need.getValue());
        assertEquals(4.0, need.getDecayRate());
        assertEquals(4.0, need.getBaseDecayRate());
        assertFalse(need.hasCriticalNotificationBeenSent());
        assertFalse(need.isCritical());
    }

    @Test
    void adjustValueAndDecayClampWithinBounds() {
        Need need = new TestNeed(NeedType.FUN, 10.0);

        need.adjustValue(50.0);
        assertEquals(100.0, need.getValue());

        need.decay(15.0);
        assertEquals(0.0, need.getValue());
        assertTrue(need.isCritical());
    }

    @Test
    void adjustSetsAbsoluteClampedValue() {
        Need need = new TestNeed(NeedType.ENERGY, 5.0);

        need.adjust(10.0);
        assertEquals(10.0, need.getValue());

        need.adjust(200.0);
        assertEquals(100.0, need.getValue());

        need.adjust(-20.0);
        assertEquals(0.0, need.getValue());
    }

    @Test
    void decayRateCanBeChangedAndRestored() {
        Need need = new TestNeed(NeedType.SOCIAL, 3.0);

        assertThrows(IllegalArgumentException.class, () -> need.setDecayRate(-1.0));

        need.setDecayRate(7.5);
        assertEquals(7.5, need.getDecayRate());

        need.restoreDefaultDecayRate();
        assertEquals(3.0, need.getDecayRate());
    }

    @Test
    void criticalNotificationFlagCanBeToggled() {
        Need need = new TestNeed(NeedType.HYGIENE, 2.0);

        need.setCriticalNotificationSent(true);
        assertTrue(need.hasCriticalNotificationBeenSent());

        need.setCriticalNotificationSent(false);
        assertFalse(need.hasCriticalNotificationBeenSent());
    }
}
