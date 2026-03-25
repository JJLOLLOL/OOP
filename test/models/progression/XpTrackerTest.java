package models.progression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class XpTrackerTest {

    @Test
    void constructorRejectsNonPositiveRequiredXp() {
        assertThrows(IllegalArgumentException.class, () -> new XpTracker(0));
        assertThrows(IllegalArgumentException.class, () -> new XpTracker(-5));
    }

    @Test
    void addProgressDoesNotAllowNegativeTotal() {
        XpTracker tracker = new XpTracker(100);

        tracker.addProgress(20);
        tracker.addProgress(-50);

        assertEquals(0, tracker.getProgress());
    }

    @Test
    void canAdvanceDependsOnRequiredXp() {
        XpTracker tracker = new XpTracker(100);

        tracker.addProgress(99.9);
        assertFalse(tracker.canAdvance());

        tracker.addProgress(0.1);
        assertTrue(tracker.canAdvance());
    }

    @Test
    void consumeRequiredXpSubtractsOnlyWhenEnoughProgressExists() {
        XpTracker tracker = new XpTracker(100);

        tracker.addProgress(150);
        tracker.consumeRequiredXP();
        assertEquals(50, tracker.getProgress());

        tracker.consumeRequiredXP();
        assertEquals(50, tracker.getProgress());
    }

    @Test
    void resetProgressClearsStoredXp() {
        XpTracker tracker = new XpTracker(100);

        tracker.addProgress(70);
        tracker.resetProgress();

        assertEquals(0, tracker.getProgress());
    }
}
