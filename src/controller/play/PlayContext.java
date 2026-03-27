package controller.play;

import core.GameState;
import core.WorldRegistry;
import data.ShopInventory;
import models.character.SimCharacter;

/**
 * Defines a context interface that provides handlers with safe, controlled access
 * to the game's core components and the ability to change the active handler.
 * This is implemented by the main PlayController.
 */
public interface PlayContext {

    /**
     * Returns the shared mutable game state.
     *
     * @return the current game state
     */
    GameState getGameState();

    /**
     * Returns the immutable world registry.
     *
     * @return the loaded world registry
     */
    WorldRegistry getWorldRegistry();

    /**
     * Returns the shop inventory available to gameplay handlers.
     *
     * @return the current shop inventory
     */
    ShopInventory getShopInventory();

    /**
     * Returns the sim currently controlled by the player.
     *
     * @return the active sim
     */
    SimCharacter getActivePlayer();

    /**
     * Switches control to another registered gameplay handler.
     *
     * @param type the handler type to activate
     */
    void switchTo(HandlerType type);

}
