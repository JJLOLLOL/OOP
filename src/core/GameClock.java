package core;

/**
 * Tracks in-game time, which advances independently of real time.
 *
 * <p>
 * The clock starts at Day 1, 08:00. Each call to {@link #tick(double)} advances
 * the accumulator; when enough real seconds have passed for one game minute
 * ({@value #REAL_SECONDS_PER_GAME_MINUTE} seconds by default), the in-game
 * minute increments.
 *
 * <p>
 * At the default rate, one real minute equals 30 in-game minutes, so a full
 * in-game day takes 48 real minutes.
 */
public class GameClock {

    /**
     * Real seconds that must elapse before one in-game minute advances.
     */
    private static final double REAL_SECONDS_PER_GAME_MINUTE = 1;

    private int days = 1;
    private int hours = 8;
    private int minutes = 0;
    private double accumulator = 0.0;

    /**
     * Creates a game clock starting at Day 1, 08:00.
     */
    public GameClock() {
    }

    // ── Tick ──────────────────────────────────────────────────────────────────
    /**
     * Advances the clock by {@code deltaTime} real seconds. Called once per
     * game-logic tick by {@link GameEngine}.
     *
     * @param deltaTime seconds elapsed since the last tick
     */
    public int tick(double deltaTime) {
        int minutesPassed = 0;
        accumulator += deltaTime;
        while (accumulator >= REAL_SECONDS_PER_GAME_MINUTE) {
            accumulator -= REAL_SECONDS_PER_GAME_MINUTE;
            minutesPassed++;
            if (++minutes >= 60) {
                minutes = 0;
                if (++hours >= 24) {
                    hours = 0;
                    days++;
                }
            }
        }
        return minutesPassed;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    /**
     * Returns the current time as an integer in HHMM format, e.g. 14:30 becomes
     * {@code 1430}. Used by {@link services.NpcService} for schedule
     * floor-entry lookups.
     *
     * @return time as HHMM integer
     */
    public int getTimeAsHHMM() {
        return hours * 100 + minutes;
    }

    /**
     * Advances the clock by the given number of in-game hours. Used by
     * work and furniture actions to skip time-consuming activities.
     *
     * @param hoursToAdd fractional in-game hours to advance (e.g. 6.5 = 6 h 30
     * m)
     */
    public void advanceHours(double hoursToAdd) {
        if (hoursToAdd <= 0) {
            return;
        }

        // Add the total minutes from the fractional hours
        int totalMinutesToAdd = (int) Math.round(hoursToAdd * 60);
        this.minutes += totalMinutesToAdd;

        // Cascade the minutes up to hours and days using calculation instead of loops
        if (this.minutes >= 60) {
            int hoursToAddFromMinutes = this.minutes / 60;
            this.minutes %= 60;

            this.hours += hoursToAddFromMinutes;
            this.days += this.hours / 24;
            this.hours %= 24;
        }
    }

    /**
     * Returns a human-readable time string, e.g. {@code "Day 1 - 08:17"}.
     *
     * @return formatted time string
     */
    public String getTimeString() {
        return String.format("Day %d - %02d:%02d", days, hours, minutes);
    }
}
