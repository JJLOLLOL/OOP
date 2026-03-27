package controller.play;

import controller.PlayController;
import models.character.SimCharacter;

import java.util.List;

/**
 * Handles choosing a different active sim during gameplay.
 */
public class SwitchCharacterHandler implements PlayInputHandler {

    private List<SimCharacter> sims;

    /**
     * Parses a sim selection and makes that sim active.
     *
     * @param input the player's raw menu selection
     * @param context the gameplay context
     * @return {@code true} when the selection succeeds
     */
    @Override
    public boolean handleInput(String input, PlayContext context) {
        if (input.equals("0")) {
            context.switchTo(HandlerType.MAIN_MENU);
            return true;
        }
        return PlayController.pickFromList(input, sims, idx -> {
            context.getGameState().setActivePlayer(sims.get(idx));
            context.switchTo(HandlerType.MAIN_MENU);
        });
    }

    /**
     * Refreshes the list of playable sims from the game state.
     *
     * @param context the gameplay context
     */
    @Override
    public void onEnter(PlayContext context) {
        this.sims = context.getGameState().getSims();
    }

    /**
     * Returns the gameplay step currently represented by this handler.
     *
     * @return {@link controller.PlayController.Step#SWITCH_CHARACTER}
     */
    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.SWITCH_CHARACTER;
    }
}
