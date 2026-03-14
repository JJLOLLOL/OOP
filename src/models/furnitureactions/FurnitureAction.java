package models.furnitureactions;

import java.util.HashMap;
import java.util.Map;
import models.SimCharacter;

public class FurnitureAction implements ActivityInterface { // Represents an action that can be performed on a piece of furniture
    private final String name;
    private final String description;
    private final Map<String, Double> affectedNeedsMap; // e.g., "Hunger" = 20.0 (restores hunger), "Energy" = -5.0 (drains energy)
    private final Map<String, Double> affectedSkillsMap; // e.g., "Cooking" = 10.0 (gains cooking skill), "Fitness" = -5.0 (loses fitness skill);
    private final double activityCost; // Money cost for action
    private final double timeRequired; // Time required for action in hours (e.g., 0.5 for 30 minutes)

    // Constructor
    public FurnitureAction(
            String name,
            String description,
            Map<String, Double> affectedNeedsMap,
            Map<String, Double> affectedSkillsMap,
            double activityCost,
            double timeRequired) {
        this.name = name;
        this.description = description;
        this.affectedNeedsMap = new HashMap<>(affectedNeedsMap);
        this.affectedSkillsMap = new HashMap<>(affectedSkillsMap);
        this.activityCost = activityCost;
        this.timeRequired = timeRequired;
    }

    @Override
    public Map<String, Double> affectedNeedsByActionMap() {
        return affectedNeedsMap;
    }

    @Override
    public Map<String, Double> affectedSkillsByActionMap() {
        return affectedSkillsMap;
    }

    @Override
    public double moneyDeducted() {
        return activityCost;
    }

    @Override
    public boolean perform(SimCharacter character) {
        if (character == null) {
            return false;
        }

        if (character.getMoney() < activityCost) {
            return false;
        }

        character.setMoney(-activityCost);

        // Apply effects on needs after action is performed
        for (Map.Entry<String, Double> effect : affectedNeedsMap.entrySet()) {
            character.adjustNeed(effect.getKey(), effect.getValue());
        }

        // Apply skill effects after action is performed
        for (Map.Entry<String, Double> effect : affectedSkillsMap.entrySet()) {
            character.addSkillProgress(effect.getKey(), effect.getValue());
        }

        // Placeholder for time simulation hook.
        double ignored = timeRequired;

        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getTimeRequired() {
        return timeRequired;
    }
}
