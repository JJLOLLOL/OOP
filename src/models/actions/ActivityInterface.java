package models.actions;

import java.util.Map;

import models.character.SimCharacter;

/**
 * Represents an activity or action that a {@link SimCharacter} can perform.
 * Defines the contract for providing information about the action's effects,
 * cost, and requirements, as well as executing it.
 */
public interface ActivityInterface {

    /**
     * Retrieves the name of the activity.
     *
     * @return the name of the activity
     */
    String getName();

    /**
     * Retrieves the description of the activity.
     *
     * @return the description of the activity
     */
    String getDescription();

    /**
     * Retrieves the time required to complete the activity.
     *
     * @return the time required in in-game hours
     */
    double getTimeRequired();

    /**
     * Returns a map of needs affected by the action.
     * The key is the need name and the value is the amount it changes
     * (positive for increase, negative for decrease).
     *
     * @return a map of affected needs and their change amounts
     */
    Map<String, Double> affectedNeedsByActionMap();

    /**
     * Returns a map of skills affected by the action.
     * The key is the skill name and the value is the amount it changes
     * (positive for increase, negative for decrease).
     *
     * @return a map of affected skills and their change amounts
     */
    Map<String, Double> affectedSkillsByActionMap();

    /**
     * Retrieves the monetary cost deducted when the activity is performed.
     *
     * @return the cost of the activity
     */
    double moneyDeducted();

    /**
     * Executes the activity for the given character.
     *
     * @param character the {@link SimCharacter} performing the activity
     * @return {@code true} if the activity was successfully performed; {@code false} otherwise
     */
    boolean perform(SimCharacter character);
}
