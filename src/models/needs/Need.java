package models.needs;

import models.SimCharacter;

/**
 * Abstract base class representing a fundamental need for a {@link SimCharacter}.
 * Needs have a value from 0 to 100, which decays over time. When a need drops
 * below a critical threshold, it triggers negative consequences.
 */
public abstract class Need {

    /** Minimum value for any need. */
    private static final double MIN_NEED_VALUE = 0.0;

    /** Maximum value for any need. */
    private static final double MAX_NEED_VALUE = 100.0;

    /** Initial value assigned to newly created needs. */
    private static final double INITIAL_NEED_VALUE = 80.0;

    /** Need value threshold below which critical consequences are triggered. */
    private static final double CRITICAL_THRESHOLD = 20.0;

    private final String needName;
    private double value; // 0 to 100
    private double decayMultiplier = 1.0;
    private final double baseDecayRate;
    private boolean criticallyLowNotified = false;

    /**
     * Constructs a new {@code Need} with a specified name and decay rate.
     *
     * @param needName  the name of the need (e.g., "Hunger", "Energy")
     * @param decayRate the amount the need decreases per game tick (must be positive)
     * @throws IllegalArgumentException if the decay rate is not positive
     */
    public Need(String needName, double decayRate) {
        this.needName = needName;
        this.value = INITIAL_NEED_VALUE;
        if (decayRate <= 0) {
            throw new IllegalArgumentException("Decay rate must be positive (greater than 0).");
        }
        this.baseDecayRate = decayRate;
    }

    /**
     * Decays the need based on its decay rate and the time elapsed.
     *
     * @param deltaTime the time elapsed since the last tick
     */
    public void decay(double deltaTime) {
        double effectiveDecayRate = baseDecayRate * decayMultiplier;
        setValue(this.value - (effectiveDecayRate * deltaTime));
    }

    /**
     * Applies a temporary multiplier to the decay rate (e.g., from debuffs).
     * This multiplier is reset each tick.
     *
     * @param multiplier the multiplier to apply to the base decay rate
     * @throws IllegalArgumentException if the multiplier is negative
     */
    public void applyDecayModifier(double multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Decay multiplier cannot be negative.");
        }
        this.decayMultiplier = multiplier;
    }

    /**
     * Resets the decay multiplier to 1.0 (no modification). Should be called at
     * the start of each tick before applying new modifiers.
     */
    public void resetDecayModifier() {
        this.decayMultiplier = 1.0;
    }

    /**
     * Gets the effective decay rate (base * multiplier) for this tick.
     *
     * @return the effective decay rate
     */
    public double getEffectiveDecayRate() {
        return baseDecayRate * decayMultiplier;
    }

    /**
     * Adjusts the need's value by a given amount, keeping it within the min and max bounds.
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
     * Defines the consequences when the need drops below the critical
     * threshold. Returns a data object describing what should happen; the
     * service applies it.
     *
     * @return a {@link CriticalConsequence} describing the effects, or null if
     * no consequences
     */
    public abstract CriticalConsequence getCriticalConsequences(SimCharacter character);

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
     * @return the current value, bounded between {@link #MIN_NEED_VALUE} and {@link #MAX_NEED_VALUE}
     */
    public double getValue() {
        return value;
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
     * Safely updates the value of the need, ensuring it remains bounded between min and max values.
     *
     * @param newValue the new target value for the need
     */
    protected void setValue(double newValue) {
        this.value = Math.max(MIN_NEED_VALUE, Math.min(MAX_NEED_VALUE, newValue));
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
