package models.need;

import models.character.SimCharacter;


/**
 * Base class for all needs that decay over time and can trigger critical
 * low-state effects.
 */
public abstract class Need {

    private static final double MIN_VALUE = 0.0;
    private static final double MAX_VALUE = 100.0;
    private static final double STARTING_VALUE = 80.0;
    private static final double CRITICAL_THRESHOLD = 20.0;
    private final NeedType type;
    private final double baseDecayRate;
    private double value;
    private double decayRate;
    private boolean criticallyLowNotifiedSent;

    /**
     * Creates a need with a starting value and base decay rate.
     *
     * @param type the need type represented by this object
     * @param decayRate the default decay rate applied over time
     */
    public Need(NeedType type, double decayRate) {
        if (type == null) {
            throw new IllegalArgumentException("Need type cannot be null.");
        }
        if (decayRate < 0) {
            throw new IllegalArgumentException("Decay rate cannot be negative.");
        }
        this.type = type;
        this.value = STARTING_VALUE;
        this.decayRate = decayRate;
        this.baseDecayRate = decayRate;
        this.criticallyLowNotifiedSent = false;
    }


    public NeedType getType() {
        return type;
    }
    public double getValue() {
        return value;
    }

    public double getDecayRate() {
        return decayRate;
    }

    public double getBaseDecayRate() {
        return baseDecayRate;
    }

    public String getNeedName() {
        return type.getName();
    }

    public boolean isCritical() {
        return value <= CRITICAL_THRESHOLD;
    }

    public void setCriticalNotificationSent(boolean sent) {
        this.criticallyLowNotifiedSent = sent;
    }

    public boolean hasCriticalNotificationBeenSent() {
        return criticallyLowNotifiedSent;
    }

    /**
     * Applies passive decay using the current decay rate.
     *
     * @param minutesPassed the elapsed in-game time in minutes
     */
    public void decay(int minutesPassed) {
        double hoursPassed = minutesPassed / 60.0;
        adjustValue(-(decayRate * hoursPassed));
    }

    /**
     * Applies a delta to the need value while clamping it to the valid range.
     *
     * @param amount the amount to add to the current value
     */
    public void adjustValue(double amount) {
        value = clamp(value + amount);
    }

    public void setDecayRate(double decayRate) {
        if (decayRate < 0) {
            throw new IllegalArgumentException("Decay rate cannot be negative.");
        }
        this.decayRate = decayRate;
    }

    /**
     * Restores the decay rate back to the original base value configured for
     * this need.
     */
    public void restoreDefaultDecayRate() {
        this.decayRate = this.baseDecayRate;
    }

    /**
     * Clamps a need value to the inclusive {@code 0..100} range.
     */
    private double clamp(double value) {
        return Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
    }

    /**
     * Advances the need for one update step and emits the critical effect only
     * once while the need remains in its critical range.
     *
     * @param sim the owning sim
     * @param minutesPassed the elapsed in-game time in minutes
     * @param decayRatePerHour the effective decay rate for this update, in points per hour
     */
    public void update(SimCharacter sim, int minutesPassed, double decayRatePerHour) {
        double hoursPassed = minutesPassed / 60.0;
        adjustValue(-(decayRatePerHour * hoursPassed));

        if (isCritical()) {
            if (criticallyLowNotifiedSent) {
                return;
            }
            onCriticallyLow(sim);
            criticallyLowNotifiedSent = true;
            return;
        }

        criticallyLowNotifiedSent = false;
    }

    /**
     * Applies the penalty for this need remaining critically low.
     *
     * @param character the affected sim
     */
    public abstract void onCriticallyLow(SimCharacter character);
}
