package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

/**
 * Handles the final confirmation prompt for the completed creation batch.
 */
public class ConfirmStepHandler implements CreationStepHandler {
    /**
     * Confirms the created sims or resets the wizard to start over.
     *
     * @param input the player's confirmation input
     * @param context the shared create-sim controller
     * @return {@code true} when the flow advances, {@code false} when an inline
     * error is shown
     */
    @Override
    public boolean handleInput(String input, CreateSimController context) {
        switch (input.toLowerCase()) {
            case "y", "yes" -> {
                context.finaliseSims(context.getGameState(), context.getWorldRegistry());
                if (context.getGameState().getSims().size() > 1) {
                    context.setStepHandler(new PickPlayerStepHandler());
                }
                // If only 1 sim, finaliseSims already changes the game phase.
            }
            case "n", "no" -> {
                context.resetCreation();
                context.setStepHandler(new CountStepHandler());
            }
            default -> {
                Renderer.showError("Enter Y to confirm or N to start over.");
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the view step represented by this handler.
     *
     * @return {@link controller.CreateSimController.Step#CONFIRM}
     */
    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.CONFIRM;
    }
}
