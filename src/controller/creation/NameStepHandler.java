package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

/**
 * Captures the name for the current sim builder.
 */
public class NameStepHandler implements CreationStepHandler {

    /**
     * Validates the entered name and advances to the age step.
     *
     * @param input the player's raw name input
     * @param context the shared create-sim controller
     * @return {@code true} when the flow advances, {@code false} when an inline
     * error is shown
     */
    @Override
    public boolean handleInput(String input, CreateSimController context) {
        if (input.isBlank()) {
            Renderer.showError("Name cannot be empty.");
            return false;
        }
        context.getCurrentBuilder().withName(input);
        context.setStepHandler(new AgeStepHandler());
        return true;
    }

    /**
     * Returns the view step represented by this handler.
     *
     * @return {@link controller.CreateSimController.Step#NAME}
     */
    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.NAME;
    }
}
