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

    /**
     * Runs any setup needed when this handler becomes active.
     *
     * @param context the gameplay context
     */
    void onEnter(PlayContext context);

    /**
     * Returns the current UI step represented by this handler.
     *
     * @return the step that should be rendered
     */
    PlayController.Step getStep();
}
