package controller.play;

import controller.PlayController;

/**
 * Defines the contract for any sub-system that can take control during the PLAYING phase.
 * Each handler is responsible for a specific feature, like shopping or interacting.
 */
public interface PlayInputHandler {

    /**
     * Processes user input for the handler's specific domain.
     * @param input The user's raw input string.
     * @param context The context providing access to game state and services.
     * @return {@code true} if the screen should be redrawn.
     */
    boolean handleInput(String input, PlayContext context);

    void onEnter(PlayContext context);

    PlayController.Step getStep();
}