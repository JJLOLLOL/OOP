package controller.creation;

import types.Gender;
import controller.CreateSimController;
import ui.Renderer;

/**
 * Resolves the selected gender for the active builder and moves to the next
 * sim or confirmation step.
 */
public class GenderStepHandler implements CreationStepHandler {

    /**
     * Parses the entered gender token and stores it on the active builder.
     *
     * @param input the player's raw gender input
     * @param context the shared create-sim controller
     * @return {@code true} when the flow advances, {@code false} when an inline
     * error is shown
     */
    @Override
    public boolean handleInput(String input, CreateSimController context) {
        try {
            Gender gender = Gender.fromUserInput(input);
            context.getCurrentBuilder().withGender(gender);
            context.advanceToNextBuilder();

            if (context.isCreationFinished()) {
                context.setStepHandler(new ConfirmStepHandler());
            } else {
                context.setStepHandler(new NameStepHandler());
            }
        } catch (IllegalArgumentException e) {
            Renderer.showError("Enter M for Male or F for Female.");
            return false;
        }
        return true;
    }

    /**
     * Returns the view step represented by this handler.
     *
     * @return {@link controller.CreateSimController.Step#GENDER}
     */
    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.GENDER;
    }
}
