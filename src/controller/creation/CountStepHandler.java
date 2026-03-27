package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

/**
 * Handles the first creation step where the player chooses how many sims to
 * create.
 */
public class CountStepHandler implements CreationStepHandler {

    private static final int MAX_SIMS = 5;

    /**
     * Parses the requested sim count and initializes builder state for that
     * many entries.
     *
     * @param input the player's raw count input
     * @param context the shared create-sim controller
     * @return {@code true} when the flow advances, {@code false} when an inline
     * error is shown
     */
    @Override
    public boolean handleInput(String input, CreateSimController context) {
        try {
            int n = Integer.parseInt(input);
            if (n < 1 || n > MAX_SIMS) {
                throw new NumberFormatException();
            }
            context.initializeBuilders(n);
            context.setStepHandler(new NameStepHandler());
        } catch (NumberFormatException e) {
            Renderer.showError("Enter a number between 1 and " + MAX_SIMS + ".");
            return false;
        }
        return true;
    }

    /**
     * Returns the view step represented by this handler.
     *
     * @return {@link controller.CreateSimController.Step#COUNT}
     */
    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.COUNT;
    }
}
