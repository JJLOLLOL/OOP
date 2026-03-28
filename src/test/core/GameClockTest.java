package core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GameClockTest {

    @Test
    void constructorStartsAtDayOneEightAm() {
        GameClock clock = new GameClock();

        assertEquals(1, clock.getDays());
        assertEquals(8, clock.getHours());
        assertEquals(0, clock.getMinutes());
        assertEquals(800, clock.getTimeAsHHMM());
        assertEquals("Day 1 - 08:00", clock.getTimeString());
    }

    @Test
    void tickAdvancesGameMinutesFromRealSeconds() {
        GameClock clock = new GameClock();

        clock.tick(0.5);
        clock.tick(1.0);

        assertEquals(8, clock.getHours());
        assertEquals(1, clock.getMinutes());
        assertEquals(801, clock.getTimeAsHHMM());
    }

    @Test
    void advanceHoursSupportsFractionsAndDayRollover() {
        GameClock clock = new GameClock();

        clock.advanceHours(1.5);
        assertEquals(9, clock.getHours());
        assertEquals(30, clock.getMinutes());

        clock.advanceHours(14.5);
        assertEquals(2, clock.getDays());
        assertEquals(0, clock.getHours());
        assertEquals(0, clock.getMinutes());
        assertEquals("Day 2 - 00:00", clock.getTimeString());
    }
}
