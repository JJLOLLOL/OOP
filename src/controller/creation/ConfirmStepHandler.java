package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

public class ConfirmStepHandler implements CreationStepHandler {
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

    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.CONFIRM;
    }
}