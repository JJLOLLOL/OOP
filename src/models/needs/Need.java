package models.needs;

import models.SimCharacter;

public abstract class Need {

    private final String needName;
    private double value;
    private double decayRate;
    private static final double CRITICAL_THRESHOLD = 20.0;
    private static final double DEFAULT_VALUE = 80.0;

    public Need(String needName, double decayRate) {
        this.needName = needName;
        this.value = DEFAULT_VALUE;
        setDecayRate(decayRate);
    }

    public void decay() {
        setValue(this.value - this.decayRate);
    }
    public void adjustNeed(double amount) {
        setValue(this.value + amount);
    }
    public boolean isCriticallyLow() {
        return this.value <= CRITICAL_THRESHOLD;
    }
    public abstract void onCriticallyLow(SimCharacter character);

    // Getters & Setters
    public String getNeedName() {
        return needName;
    }
    public double getValue() {
        return value;
    }
    public double getDecayRate() {
        return decayRate;
    }
    protected void setValue(double newValue) {
        this.value = Math.max(0, Math.min(100, newValue));
    }
    public void setDecayRate(double decayRate) {
        if (decayRate < 0) {
            throw new IllegalArgumentException("Decay rate cannot be negative.");
        }
        this.decayRate = decayRate;
    }
}