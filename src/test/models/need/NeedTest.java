package models.need;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import models.character.SimCharacter;
import models.location.Location;

class NeedTest {

    private static class TestNeed extends Need {
        private boolean criticallyLowTriggered = false;

        TestNeed(NeedType type, double decayRate) {
            super(type, decayRate);
        }

        @Override
        public void onCriticallyLow(SimCharacter character) {
            criticallyLowTriggered = true;
        }

        boolean wasCriticallyLowTriggered() {
            return criticallyLowTriggered;
        }

        void resetTrigger() {
            criticallyLowTriggered = false;
        }
    }

    @Test
    void constructor_setsFieldsCorrectly() {
        TestNeed need = new TestNeed(NeedType.HUNGER, 8.0);

        assertEquals(NeedType.HUNGER, need.getType());
        assertEquals("Hunger", need.getNeedName());
        assertEquals(80.0, need.getValue());
        assertEquals(8.0, need.getDecayRate());
        assertEquals(8.0, need.getBaseDecayRate());
        assertFalse(need.hasCriticalNotificationBeenSent());
    }

    @Test
    void constructor_throwsExceptionWhenTypeIsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new TestNeed(null, 5.0));

        assertEquals("Need type cannot be null.", ex.getMessage());
    }

    @Test
    void constructor_throwsExceptionWhenDecayRateIsNegative() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new TestNeed(NeedType.FUN, -1.0));

        assertEquals("Decay rate cannot be negative.", ex.getMessage());
    }

    @Test
    void adjustValue_increasesAndClampsAtHundred() {
        TestNeed need = new TestNeed(NeedType.ENERGY, 8.0);

        need.adjustValue(50.0);

        assertEquals(100.0, need.getValue());
    }

    @Test
    void adjustValue_decreasesAndClampsAtZero() {
        TestNeed need = new TestNeed(NeedType.ENERGY, 8.0);

        need.adjustValue(-200.0);

        assertEquals(0.0, need.getValue());
    }

    @Test
    void decay_reducesValueByDecayRateTimesDeltaTime() {
        TestNeed need = new TestNeed(NeedType.HYGIENE, 3.0);

        need.decay(2);

        assertEquals(74.0, need.getValue());
    }

    @Test
    void isCritical_returnsTrueAtOrBelowTwenty() {
        TestNeed need = new TestNeed(NeedType.SOCIAL, 3.0);

        need.adjustValue(-60.0);
        assertTrue(need.isCritical());

        need.adjustValue(1.0);
        assertFalse(need.isCritical());
    }

    @Test
    void setDecayRate_updatesDecayRate() {
        TestNeed need = new TestNeed(NeedType.FUN, 3.0);

        need.setDecayRate(7.5);

        assertEquals(7.5, need.getDecayRate());
        assertEquals(3.0, need.getBaseDecayRate());
    }

    @Test
    void setDecayRate_throwsExceptionWhenNegative() {
        TestNeed need = new TestNeed(NeedType.FUN, 3.0);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> need.setDecayRate(-0.1));

        assertEquals("Decay rate cannot be negative.", ex.getMessage());
    }

    @Test
    void restoreDefaultDecayRate_resetsToBaseDecayRate() {
        TestNeed need = new TestNeed(NeedType.FUN, 3.0);

        need.setDecayRate(9.0);
        need.restoreDefaultDecayRate();

        assertEquals(3.0, need.getDecayRate());
    }

    @Test
    void criticalNotificationFlag_canBeSetAndRead() {
        TestNeed need = new TestNeed(NeedType.HUNGER, 8.0);

        need.setCriticalNotificationSent(true);
        assertTrue(need.hasCriticalNotificationBeenSent());

        need.setCriticalNotificationSent(false);
        assertFalse(need.hasCriticalNotificationBeenSent());
    }

    @Test
    void update_triggersOnCriticallyLowOnlyOnceUntilRecovered() {
        TestNeed need = new TestNeed(NeedType.HUNGER, 8.0);
        SimCharacter sim = new SimCharacter("Alex", 20, types.Gender.MALE,
                new Location("Home", null));

        need.adjustValue(-60.0); // 80 -> 20, critical
        need.update(sim, 0, 8.0);

        assertTrue(need.wasCriticallyLowTriggered());
        assertTrue(need.hasCriticalNotificationBeenSent());

        need.resetTrigger();
        need.update(sim, 0, 8.0);

        assertFalse(need.wasCriticallyLowTriggered());
        assertTrue(need.hasCriticalNotificationBeenSent());

        need.adjustValue(10.0); // recover above critical
        need.update(sim, 0, 8.0);

        assertFalse(need.hasCriticalNotificationBeenSent());

        need.adjustValue(-15.0); // critical again
        need.update(sim, 0, 8.0);

        assertTrue(need.wasCriticallyLowTriggered());
        assertTrue(need.hasCriticalNotificationBeenSent());
    }

    @Test
    void update_usesPassedDecayRateInsteadOfStoredDecayRate() {
        TestNeed need = new TestNeed(NeedType.ENERGY, 8.0);
        SimCharacter sim = new SimCharacter("Jamie", 21, types.Gender.FEMALE,
                new Location("Room", null));

        need.update(sim, 2, 5.0);

        assertEquals(70.0, need.getValue());
    }
}