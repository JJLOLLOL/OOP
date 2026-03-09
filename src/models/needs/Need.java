package models.needs;

import models.SimCharacter;

public abstract class Need {

    private final String needName;
    private double value; // 0 to 100
    private double decayRate; // How much it decreases per tick
    private static final double CRITICAL_THRESHOLD = 20.0; // Below this, the need is critically low

    // Constructor
    public Need(String needName, double decayRate) {
        this.needName = needName;
        this.value = 80.0; // Starting value for the sim character
        if (decayRate < 0) {
            throw new IllegalArgumentException("Decay rate cannot be negative.");
        }
        this.decayRate = decayRate;
    }

    // Each tick, the need decays by its decay rate
    public void decay() {
        setValue(this.value - this.decayRate);
    }

    // When an activity is performed, it can adjust the need by a certain amount
    public void adjustNeed(double amount) {
        setValue(this.value + amount);
    }

    // Check if the need is critically low
    public boolean isCriticallyLow() {
        return this.value <= CRITICAL_THRESHOLD;
    }

    // If the need drops below a critical threshold, it can trigger negative consequences or messages
    public abstract void onCriticallyLow(SimCharacter character);

    // Getters
    public String getNeedName() {
        return needName;
    }

    public double getValue() {
        return value;
    }

    public double getDecayRate() {
        return decayRate;
    }

    // Setters
    protected void setValue(double newValue) {
        this.value = Math.max(0, Math.min(100, newValue)); // Ensure value stays between 0 and 100
    }

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
