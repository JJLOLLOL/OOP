package models;
public class Need {
    private String name;
    private double value; // 0 to 100
    private double decayRate; // How much it decreases per tick

    public Need(String name, double decayRate) {
        this.name = name;
        this.value = 80.0; // Starting value for the sim character
        this.decayRate = decayRate;
    }

    public void decay() {
        this.value -= this.decayRate;
        if (this.value < 0) this.value = 0;
    }

    public void increase(double amount) {
        this.value += amount;
        if (this.value > 100) this.value = 100; // No More than 100
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        // This is the visual bar: [#####.....]
        int bars = (int) (value / 10);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < bars) bar.append("#");
            else bar.append("-");
        }
        bar.append("] ").append((int)value).append("/100");
        return String.format("%-10s %s", name, bar.toString());
    }
}
