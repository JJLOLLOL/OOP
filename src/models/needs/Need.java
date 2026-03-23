package models.needs;

import models.SimCharacter;

/**
 * Abstract base class representing a fundamental need for a {@link SimCharacter}.
 * Needs have a value from 0 to 100, which decays over time. When a need drops
 * below a critical threshold, it triggers negative consequences.
 */
public abstract class Need {

    private final String needName;
    private double value; // 0 to 100
    private double decayRate; // How much it decreases per tick
    private final double baseDecayRate;
    private static final double CRITICAL_THRESHOLD = 20.0; // Below this, the need is critically low
    private boolean criticallyLowNotified = false;

    /**
     * Constructs a new {@code Need} with a specified name and decay rate.
     *
     * @param needName  the name of the need (e.g., "Hunger", "Energy")
     * @param decayRate the amount the need decreases per game tick
     * @throws IllegalArgumentException if the decay rate is negative
     */
    public Need(String needName, double decayRate) {
        this.needName = needName;
        this.value = 80.0; // Starting value for the sim character
        if (decayRate < 0) {
            throw new IllegalArgumentException("Decay rate cannot be negative.");
        }
        this.decayRate = decayRate;
        this.baseDecayRate = decayRate;
    }

    /**
     * Decays the need based on its decay rate and the time elapsed.
     *
     * @param deltaTime the time elapsed since the last tick
     */
    public void decay(double deltaTime) {
        setValue(this.value - (this.decayRate * deltaTime));
    }

    /**
     * Adjusts the need's value by a given amount, keeping it within the 0 to 100 bounds.
     *
     * @param amount the amount to add to (or subtract from, if negative) the need's value
     */
    public void adjustNeed(double amount) {
        setValue(this.value + amount);
    }

    /**
     * Checks if the need's value has dropped to or below the critical threshold.
     *
     * @return {@code true} if the need is critically low; {@code false} otherwise
     */
    public boolean isCriticallyLow() {
        return this.value <= CRITICAL_THRESHOLD;
    }

    /**
     * Triggered when the need drops below the critical threshold.
     * Implemented by subclasses to apply specific negative effects or notifications.
     *
     * @param character the {@link SimCharacter} experiencing the critically low need
     */
    public abstract void onCriticallyLow(SimCharacter character);

    /**
     * Retrieves the name of the need.
     *
     * @return the need name
     */
    public String getNeedName() {
        return needName;
    }

    /**
     * Retrieves the current value of the need.
     *
     * @return the current value, bounded between 0 and 100
     */
    public double getValue() {
        return value;
    }

    /**
     * Retrieves the current decay rate of the need.
     *
     * @return the decay rate
     */
    public double getDecayRate() {
        return decayRate;
    }

    /**
     * Retrieves the base decay rate of the need.
     *
     * @return the base decay rate
     */
    public double getBaseDecayRate() {
        return baseDecayRate;
    }

    /**
     * Checks if the critically low notification has already been triggered.
     *
     * @return {@code true} if notified; {@code false} otherwise
     */
    public boolean isCriticallyLowNotified() {
        return criticallyLowNotified;
    }

    /**
     * Sets whether the critically low notification has been triggered.
     *
     * @param criticallyLowNotified {@code true} to mark as notified; {@code false} to reset
     */
    public void setCriticallyLowNotified(boolean criticallyLowNotified) {
        this.criticallyLowNotified = criticallyLowNotified;
    }

    /**
     * Safely updates the value of the need, ensuring it remains bounded between 0 and 100.
     *
     * @param newValue the new target value for the need
     */
    protected void setValue(double newValue) {
        this.value = Math.max(0, Math.min(100, newValue)); // Ensure value stays between 0 and 100
    }

    /**
     * Sets the current decay rate of the need.
     *
     * @param decayRate the new decay rate
     * @throws IllegalArgumentException if the decay rate is negative
     */
    public void setDecayRate(double decayRate) {

        if (decayRate < 0) {
            throw new IllegalArgumentException("Decay rate cannot be negative.");
        }
        this.decayRate = decayRate;
    }

    @Override
    public String toString() {
        // This is the visual bar: [#####.....]
        int bars = (int) Math.round(value / 10);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < bars)
                bar.append("#");
            else
                bar.append("-");
        }
        bar.append("] ").append((int) value).append("/100");
        return String.format("%-10s %s", needName, bar.toString());
    }
}
