package controller.play;

import controller.PlayController;
import core.GameState;
import ui.Renderer;

public class MainMenuHandler implements PlayInputHandler {
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

    @Override
    public void onEnter(PlayContext context) {
        // No setup needed for main menu
    }

    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.MAIN;
    }
}