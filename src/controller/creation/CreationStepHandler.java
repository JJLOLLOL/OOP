package controller.creation;

import controller.CreateSimController;

/**
 * Defines the contract for a single step in the creation wizard.
 * Each handler is responsible for validating its own input, updating the
 * shared context, and transitioning to the next step.
 */
public interface CreationStepHandler {

    /**
     * Processes user input for the specific step this handler represents.
     *
     * @param input   The user's raw input string.
     * @param context The {@link CreateSimController} which acts as the context,
     *                holding shared state and providing methods for transition.
     * @return {@code true} if the step changed and a redraw is needed,
     *         {@code false} for an inline error.
     */
    boolean handleInput(String input, CreateSimController context);

    /**
     * Returns the corresponding {@link CreateSimController.Step} enum value for this handler.
     * This is used to inform the {@link ui.Renderer} what to display.
     *
     * @return The enum step value.
     */
    CreateSimController.Step getStep();
}