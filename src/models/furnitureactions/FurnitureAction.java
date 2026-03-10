package models.furnitureactions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import models.SimCharacter;

public class FurnitureAction implements ActivityInterface {
    private final String name;
    private final String description;
    private final Map<String, Double> needsEffect;
    private final Map<String, Double> skillsEffect;
    private final double activityCost;
    private final double timeRequired;

    // Constructor
    public FurnitureAction(
            String name,
            String description,
            Map<String, Double> needsEffect,
            Map<String, Double> skillsEffect,
            double activityCost,
            double timeRequired) {
        this.name = name;
        this.description = description;
        this.needsEffect = Collections.unmodifiableMap(new HashMap<>(needsEffect));
        this.skillsEffect = Collections.unmodifiableMap(new HashMap<>(skillsEffect));
        this.activityCost = activityCost;
        this.timeRequired = timeRequired;
    }

    @Override
    public Map<String, Double> affectedNeeds() {
        return needsEffect;
    }

    @Override
    public Map<String, Double> affectedSkills() {
        return skillsEffect;
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

        // Apply need effects
        for (Map.Entry<String, Double> effect : needsEffect.entrySet()) {
            character.adjustNeed(effect.getKey(), effect.getValue());
        }

        // Apply skill effects
        for (Map.Entry<String, Double> effect : skillsEffect.entrySet()) {
            character.addSkillProgress(effect.getKey(), effect.getValue());
        }

        // Placeholder for time simulation hook.
        double ignored = timeRequired;

        return true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getTimeRequired() {
        return timeRequired;
    }
}
