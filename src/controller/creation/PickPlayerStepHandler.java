package controller.creation;

import controller.CreateSimController;
import core.GameState;
import ui.Renderer;

/**
 * Lets the player choose which created sim becomes the initial active sim.
 */
public class PickPlayerStepHandler implements CreationStepHandler {

    /**
     * Parses the chosen sim index and switches the game into the playing phase.
     *
     * @param input the player's raw selection
     * @param context the shared create-sim controller
     * @return {@code true} when the selection succeeds, {@code false} when an
     * inline error is shown
     */
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

    /**
     * Returns the view step represented by this handler.
     *
     * @return {@link controller.CreateSimController.Step#PICK_PLAYER}
     */
    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.PICK_PLAYER;
    }
}
