package core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GameClockTest {

    @Test
    void tickDoesNotAdvanceUntilEnoughRealTimeHasPassed() {
        GameClock clock = new GameClock();

        clock.tick(0.49);

        assertEquals(1, clock.getDays());
        assertEquals(8, clock.getHours());
        assertEquals(0, clock.getMinutes());
    }

    @Test
    void tickAdvancesMultipleMinutesAndRollsOverToNextHour() {
        GameClock clock = new GameClock();

        clock.tick(30.0);

        assertEquals(1, clock.getDays());
        assertEquals(9, clock.getHours());
        assertEquals(0, clock.getMinutes());
        assertEquals(900, clock.getTimeAsHHMM());
    }

    @Test
    void tickRollsOverToNextDay() {
        GameClock clock = new GameClock();

        clock.tick(16 * 60 * 0.5);

        assertEquals(2, clock.getDays());
        assertEquals(0, clock.getHours());
        assertEquals(0, clock.getMinutes());
    }

    @Test
    void advanceHoursSupportsFractionalHours() {
        GameClock clock = new GameClock();

        clock.advanceHours(6.5);

        assertEquals(1, clock.getDays());
        assertEquals(14, clock.getHours());
        assertEquals(30, clock.getMinutes());
        assertEquals("Day 1 - 14:30", clock.getTimeString());
    }
}
