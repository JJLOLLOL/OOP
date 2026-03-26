package controller.creation;

import controller.CreateSimController;
import core.GameState;
import ui.Renderer;

public class PickPlayerStepHandler implements CreationStepHandler {

    @Override
    public boolean handleInput(String input, CreateSimController context) {
        GameState state = context.getGameState();
        try {
            int pick = Integer.parseInt(input) - 1;
            if (pick < 0 || pick >= state.getSims().size()) {
                throw new NumberFormatException();
            }
            state.setActivePlayer(state.getSims().get(pick));
            state.setPhase(GameState.Phase.PLAYING);
        } catch (NumberFormatException e) {
            Renderer.showError("Enter a number between 1 and " + state.getSims().size() + ".");
            return false;
        }
        return true;
    }

    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.PICK_PLAYER;
    }
}