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

    GameState getGameState();

    WorldRegistry getWorldRegistry();

    ShopInventory getShopInventory();

    SimCharacter getActivePlayer();

    void switchTo(HandlerType type);

}