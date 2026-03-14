package models.furnitureactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Furniture {
    private final String name;
    private final String description;

    // Map containing actions that can be performed on this furniture, action name as key and FurnitureAction as value
    private final Map<String, FurnitureAction> availableActionsMap; 

    // Constructor
    public Furniture(String name, String description) {
        this.name = name;
        this.description = description;
        this.availableActionsMap = new HashMap<>();
    }

    public void addAction(FurnitureAction action) {
        if (action != null) {
            availableActionsMap.put(action.getName(), action);
        }
    }

    // Retrieves a FurnitureAction by its name. Returns null if no such action exists.
    public FurnitureAction getAction(String actionName) {
        return availableActionsMap.get(actionName);
    }

    // Performs the specified action on the given character. Returns true if the action was successfully performed, false otherwise.
    public boolean performAction(String actionName, models.SimCharacter character) {
        FurnitureAction action = availableActionsMap.get(actionName);
        if (action == null) {
            return false;
        }
        return action.perform(character);
    }

    public List<String> getActionNames() {
        return new ArrayList<>(availableActionsMap.keySet());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
