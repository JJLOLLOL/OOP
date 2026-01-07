package models;
import java.util.HashMap;
import java.util.Map;

public class Activity {
    private String name;
    private int durationMinutes;
    private Map<String, Double> needEffects; // e.g., "Hunger" -> 20.0 (restores hunger), "Energy" -> -5.0 (drains energy)

    public Activity(String name, int durationMinutes) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.needEffects = new HashMap<>();
    }

    public void addEffect(String needName, double amount) {
        needEffects.put(needName, amount);
    }

    public String getName() {
        return name;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public Map<String, Double> getNeedEffects() {
        return needEffects;
    }
}
