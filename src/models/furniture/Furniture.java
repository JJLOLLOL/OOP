package models.furniture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a piece of furniture in the game world that a character can interact with.
 * Each furniture item has a name, description, price, and a set of available actions.
 */
public class Furniture {
    private final String name;
    private final String description;
    private final double price;

    /**
     * Map containing actions that can be performed on this furniture.
     * The action name serves as the key, and the {@link FurnitureAction} as the value.
     */
    private final Map<String, FurnitureAction> availableActionsMap;

    /**
     * Constructs a new {@code Furniture} item.
     *
     * @param name        the name of the furniture
     * @param description the description of the furniture
     * @param price       the cost to purchase the furniture
     */
    public Furniture(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableActionsMap = new HashMap<>();
    }

    /**
     * Adds a new action that can be performed on this furniture.
     *
     * @param action the {@link FurnitureAction} to add
     */
    public void addAction(FurnitureAction action) {
        if (action != null) {
            availableActionsMap.put(action.getName(), action);
        }
    }

    /**
     * Retrieves a {@link FurnitureAction} by its name.
     *
     * @param actionName the name of the action to retrieve
     * @return the corresponding {@link FurnitureAction}, or {@code null} if no such action exists
     */
    public FurnitureAction getAction(String actionName) {
        return availableActionsMap.get(actionName);
    }

    /**
     * Retrieves a list of all available actions for this furniture.
     *
     * @return a list of all {@link FurnitureAction} instances associated with this furniture
     */
    public List<FurnitureAction> getActions() {
        return new ArrayList<>(availableActionsMap.values());
    }

    /**
     * Performs the specified action on the given character.
     *
     * @param actionName the name of the action to perform
     * @param character  the {@link models.character.SimCharacter} performing the action
     * @return {@code true} if the action was successfully performed, {@code false} otherwise
     */
    public boolean performAction(String actionName, models.character.SimCharacter character) {
        FurnitureAction action = availableActionsMap.get(actionName);
        if (action == null) {
            return false;
        }
        return action.perform(character);
    }

    /**
     * Retrieves a list of the names of all available actions for this furniture.
     *
     * @return a list of available action names
     */
    public List<String> getActionNames() {
        return new ArrayList<>(availableActionsMap.keySet());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
