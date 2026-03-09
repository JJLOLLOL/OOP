package models;

import java.util.HashMap;
import java.util.Map;
import Types.InteractionType;

public class NPCCharacter extends Character {
    private Map<Integer, Activity> schedule;

    public NPCCharacter(String name, int agegroup, String gender) {
        super(name, agegroup, gender, "Home");
        this.schedule = new HashMap<>();
    }

    @Override
    public void update(int minutesPassed) {
        int currentHour = (minutesPassed / 60) % 24;
        Activity scheduledActivity = getScheduledActivity(currentHour);
        if (scheduledActivity != null) {
            updateLocationBasedOnActivity(scheduledActivity);
        }
    }
    
    private void updateLocationBasedOnActivity(Activity activity) {
        String activityName = activity.getName().toLowerCase();
        
        if (activityName.contains("work") || activityName.contains("job")) {
            setLocation("Work");
        } else if (activityName.contains("sleep") || activityName.contains("rest")) {
            setLocation("Home");
        } else if (activityName.contains("eat")) {
            setLocation("Restaurant");
        } else if (activityName.contains("gym") || activityName.contains("exercise")) {
            setLocation("Gym");
        } else {
            setLocation(activity.getName());
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("[NPC] " + getName() + " | Location: " + getLocation());
    }

    public void interact(SimCharacter sim, InteractionType interactionType) {
        reactToInteraction(interactionType);
    }

    public void reactToInteraction(InteractionType interactionType) {
        switch (interactionType) {
            case TALK:
                System.out.println(getName() + " responds positively to the conversation.");
                break;
            case COMPLIMENT:
                System.out.println(getName() + " smiles and thanks you warmly.");
                break;
            case ARGUE:
                System.out.println(getName() + " responds negatively and walks away.");
                break;
            case INSULT:
                System.out.println(getName() + " looks hurt and storms off angrily.");
                break;
        }
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
