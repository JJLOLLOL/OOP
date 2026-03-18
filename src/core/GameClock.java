package core;

public class GameClock {

    private int days;
    private int hours;
    private int minutes;

    private double accumulator;
    private final double realSecondsPerGameMinute;

    public GameClock() {
        this.days = 1;
        this.hours = 8;
        this.minutes = 0;
        this.accumulator = 0.0;
        this.realSecondsPerGameMinute = 2.0;
    }

    public void tick(double deltaTime) {
        accumulator += deltaTime;
        while (accumulator >= realSecondsPerGameMinute) {
            accumulator -= realSecondsPerGameMinute;
            minutes++;
            if (minutes >= 60) {
                minutes = 0;
                hours++;
                if (hours >= 24) {
                    hours = 0;
                    days++;
                }
            }
        }
    }

    public String getTimeString() {
        return String.format("Day %d - %02d:%02d", days, hours, minutes);
    }

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
     * Returns hours*100 + minutes as an integer (e.g. 14:30 → 1430) for
     * schedule lookups.
     */
    public int getTimeAsHHMM() {
        return hours * 100 + minutes;
    }
}
