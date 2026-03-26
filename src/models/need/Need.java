package models.need;

import models.character.SimCharacter;


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
    public void decay(double deltaTime) {
        adjustValue(-(decayRate * deltaTime));
    }

    public void adjustValue(double amount) {
        value = clamp(value + amount);
    }

    public void setDecayRate(double decayRate) {
        if (decayRate < 0) {
            throw new IllegalArgumentException("Decay rate cannot be negative.");
        }
        this.decayRate = decayRate;
    }

    public void restoreDefaultDecayRate() {
        this.decayRate = this.baseDecayRate;
    }

    private double clamp(double value) {
        return Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
    }
    public void update(SimCharacter sim, double deltaTime, double decayRate) {
        adjustValue(-(decayRate * deltaTime));

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

    public abstract void onCriticallyLow(SimCharacter character);
}
