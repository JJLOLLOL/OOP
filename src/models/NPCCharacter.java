package models;

import java.util.HashMap;
import java.util.Map;

public class NPCCharacter extends Character {
    private Map<Integer, Activity> schedule;

    public NPCCharacter(String name, int agegroup, String gender, Location defaultLocation) {
        super(name, agegroup, gender, defaultLocation);
        this.schedule = new HashMap<>();
    }

    public void scheduleActivity(int hour, Activity activity) {
        if (hour >= 0 && hour < 24) {
            schedule.put(hour, activity);
        }
    }

    public Activity getScheduledActivity(int hour) {
        return schedule.getOrDefault(hour, null);
    }

    public Map<Integer, Activity> getSchedule() {
        return schedule;
    }
}
