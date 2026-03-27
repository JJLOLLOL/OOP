package controller.play;

import controller.PlayController;
import core.GameState;
import ui.Renderer;

/**
 * Handles the top-level gameplay menu.
 */
public class MainMenuHandler implements PlayInputHandler {
    /**
     * Dispatches the selected top-level menu option.
     *
     * @param input the player's raw menu selection
     * @param context the gameplay context
     * @return {@code true} when the menu changed or the game quit
     */
    @Override
    public boolean handleInput(String input, PlayContext context) {
        switch (input) {
            case "1" -> context.switchTo(HandlerType.INTERACTION);
            case "2" -> context.switchTo(HandlerType.SOCIAL);
            case "3" -> context.switchTo(HandlerType.LOCATION_CHANGE);
            case "4" -> context.switchTo(HandlerType.SWITCH_CHARACTER);
            case "5" -> context.switchTo(HandlerType.SHOP);
            case "6" -> context.getGameState().setPhase(GameState.Phase.QUIT);
            default -> {
                Renderer.showError("Invalid choice. Enter 1-6.");
                return false;
            }
        }
        return true;
    }

    /**
     * Performs no setup because the main menu is stateless.
     *
     * @param context the gameplay context
     */
    @Override
    public void onEnter(PlayContext context) {
        // No setup needed for main menu
    }

    /**
     * Returns the gameplay step currently represented by this handler.
     *
     * @return {@link controller.PlayController.Step#MAIN}
     */
    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.MAIN;
    }
}
