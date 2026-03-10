package models.furnitureactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Furniture {
    private final String name; // name of furniture
    private final String description; // description of furniture
    private final Map<String, FurnitureAction> actions; // map action name to FurnitureAction

    // Constructor
    public Furniture(String name, String description) {
        this.name = name;
        this.description = description;
        this.actions = new HashMap<>();
    }

    public void addAction(FurnitureAction action) {
        if (action != null) {
            actions.put(action.getName(), action);
        }
    }

    public FurnitureAction getAction(String actionName) {
        return actions.get(actionName);
    }

    public boolean performAction(String actionName, models.SimCharacter character) {
        FurnitureAction action = actions.get(actionName);
        if (action == null) {
            return false;
        }
        return action.perform(character);
    }

    public List<String> getActionNames() {
        return new ArrayList<>(actions.keySet());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Furniture createBed() {
        Furniture bed = new Furniture("Bed", "A comfortable bed for resting");
        bed.addAction(new FurnitureAction(
                "Nap",
                "A short rest to recover energy.",
                Map.of("Energy", 15.0, "Hunger", -2.0),
                Map.of(),
                0.0,
                1.0));
        bed.addAction(new FurnitureAction(
                "Sleep",
                "A full sleep for big energy recovery.",
                Map.of("Energy", 35.0, "Hunger", -5.0),
                Map.of(),
                0.0,
                8.0));
        return bed;
    }

    public static Furniture createOven() {
        Furniture oven = new Furniture("Oven", "A kitchen oven for cooking");
        oven.addAction(new FurnitureAction(
                "Cook Quick Meal",
                "Prepare a simple meal.",
                Map.of("Hunger", 20.0, "Energy", -5.0),
                Map.of("Cooking", 8.0),
                5.0,
                0.5));
        oven.addAction(new FurnitureAction(
                "Bake Meal",
                "Cook a proper baked meal.",
                Map.of("Hunger", 30.0, "Energy", -8.0),
                Map.of("Cooking", 15.0),
                8.0,
                1.0));
        return oven;
    }

}
