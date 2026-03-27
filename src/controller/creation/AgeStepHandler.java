package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

/**
 * Validates the age entered for the current sim and advances to gender
 * selection.
 */
public class AgeStepHandler implements CreationStepHandler {

    private final int MAX_AGE = 90;
    private final int MIN_AGE = 10;

    /**
     * Parses the entered age, validates its allowed range, and stores it on the
     * active builder.
     *
     * @param input the player's raw age input
     * @param context the shared create-sim controller
     * @return {@code true} when the flow advances, {@code false} when an inline
     * error is shown
     */
    @Override
    public boolean handleInput(String input, CreateSimController context) {
        try {
            int age = Integer.parseInt(input);
            if (age < MIN_AGE || age > MAX_AGE) {
                throw new NumberFormatException();
            }
            context.getCurrentBuilder().withAge(age);
            context.setStepHandler(new GenderStepHandler());
        } catch (NumberFormatException e) {
            Renderer.showError(String.format("Age must be a number between %d and %d.", MIN_AGE, MAX_AGE));
            return false;
        }
        return true;
    }

    /**
     * Returns the view step represented by this handler.
     *
     * @return {@link controller.CreateSimController.Step#AGE}
     */
    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.AGE;
    }
}
