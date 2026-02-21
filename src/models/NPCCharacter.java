package models;

import java.util.HashMap;
import java.util.Map;

public class NPCCharacter extends Character {
    private String relationshipStatus;
    private int relationshipScore; 
    private Map<Integer, Activity> schedule; 

    public NPCCharacter(String name, int agegroup, String gender) {
        super(name, agegroup, gender);
        this.relationshipScore = 0;
        this.relationshipStatus = "Stranger";
        this.schedule = new HashMap<>();
    }

    @Override
    public void update(int minutesPassed) {
        int currentHour = (minutesPassed / 60) % 24;
        Activity scheduledActivity = getScheduledActivity(currentHour);
        if (scheduledActivity != null) {
            updateLocationBasedOnActivity(scheduledActivity);
        }
        decayRelationships();
    }
    
    private void updateLocationBasedOnActivity(Activity activity) {
        String activityName = activity.getName().toLowerCase();
        
        if (activityName.contains("work") || activityName.contains("job")) {
            setLocation("Work"); // Could be Hospital, Office, etc.
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
        System.out.println("[NPC] " + name + " | Rel: " + relationshipStatus + " (" + relationshipScore + ")");
    }

    public void interact(SimCharacter sim, String interactionType) {
        if (interactionType.equals("Talk")) {
            relationshipScore += 5;
            System.out.println(sim.getName() + " talked to " + name + ". Relationship improved.");
        } else if (interactionType.equals("Argue")) {
            relationshipScore -= 10;
            System.out.println(sim.getName() + " argued with " + name + ". Relationship worsened.");
        }
        updateRelationshipStatus();
        reactToInteraction(interactionType);
    }

    public void reactToInteraction(String interactionType) {
        if (interactionType.equals("Talk")) {
            System.out.println(name + " responds positively to the conversation.");
        } else if (interactionType.equals("Argue")) {
            System.out.println(name + " responds negatively and walks away.");
        }
    }

    private void updateRelationshipStatus() {
        if (relationshipScore > 50)
            relationshipStatus = "Friend";
        else if (relationshipScore > 0)
            relationshipStatus = "Acquaintance";
        else if (relationshipScore > -25)
            relationshipStatus = "Stranger"; // 0 to -25
        else if (relationshipScore > -100)
            relationshipStatus = "Enemy";
        else
            relationshipStatus = "Nemesis";
    }

    public void decayRelationships() {
        if (relationshipScore > 0) {
            relationshipScore--;
        }
        updateRelationshipStatus();
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
