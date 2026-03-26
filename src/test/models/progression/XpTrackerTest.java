package models.progression;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class XpTrackerTest {

    @Test
    void constructor_setsDefaultProgressAndRequiredXp() {
        XpTracker tracker = new XpTracker(100.0);

        assertEquals(0.0, tracker.getProgress());
        assertEquals(100.0, tracker.getRequiredXP());
    }

    @Test
    void constructor_throwsExceptionWhenRequiredXpIsZero() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new XpTracker(0.0));

        assertEquals("Required XP must be more than 0", ex.getMessage());
    }

    @Test
    void constructor_throwsExceptionWhenRequiredXpIsNegative() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new XpTracker(-5.0));

        assertEquals("Required XP must be more than 0", ex.getMessage());
    }

    @Test
    void setRequiredXp_updatesValue() {
        XpTracker tracker = new XpTracker(100.0);

        tracker.setRequiredXP(250.0);

        assertEquals(250.0, tracker.getRequiredXP());
    }

    @Test
    void setRequiredXp_throwsExceptionWhenValueIsZero() {
        XpTracker tracker = new XpTracker(100.0);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> tracker.setRequiredXP(0.0));

        assertEquals("Required XP must be more than 0", ex.getMessage());
    }

    @Test
    void addProgress_increasesProgress() {
        XpTracker tracker = new XpTracker(100.0);

        tracker.addProgress(40.0);

        assertEquals(40.0, tracker.getProgress());
    }

    @Test
    void addProgress_doesNotAllowNegativeProgress() {
        XpTracker tracker = new XpTracker(100.0);

        tracker.addProgress(20.0);
        tracker.addProgress(-50.0);

        assertEquals(0.0, tracker.getProgress());
    }

    @Test
    void canAdvance_returnsFalseWhenProgressBelowRequiredXp() {
        XpTracker tracker = new XpTracker(100.0);
        tracker.addProgress(99.9);

        assertFalse(tracker.canAdvance());
    }

    @Test
    void canAdvance_returnsTrueWhenProgressEqualsRequiredXp() {
        XpTracker tracker = new XpTracker(100.0);
        tracker.addProgress(100.0);

        assertTrue(tracker.canAdvance());
    }

    @Test
    void canAdvance_returnsTrueWhenProgressExceedsRequiredXp() {
        XpTracker tracker = new XpTracker(100.0);
        tracker.addProgress(150.0);

        assertTrue(tracker.canAdvance());
    }

    @Test
    void consumeRequiredXp_subtractsRequiredXpWhenEnoughProgress() {
        XpTracker tracker = new XpTracker(100.0);
        tracker.addProgress(150.0);

        tracker.consumeRequiredXP();

        assertEquals(50.0, tracker.getProgress());
    }

    @Test
    void consumeRequiredXp_doesNothingWhenNotEnoughProgress() {
        XpTracker tracker = new XpTracker(100.0);
        tracker.addProgress(60.0);

        tracker.consumeRequiredXP();

        assertEquals(60.0, tracker.getProgress());
    }

    @Test
    void resetProgress_setsProgressToZero() {
        XpTracker tracker = new XpTracker(100.0);
        tracker.addProgress(80.0);

        tracker.resetProgress();

        assertEquals(0.0, tracker.getProgress());
    }
}