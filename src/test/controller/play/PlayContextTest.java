package controller.play;

import core.GameState;
import core.WorldRegistry;
import data.ShopInventory;
import models.character.SimCharacter;

class PlayContextTest implements PlayContext {
    private final GameState gameState;
    private final WorldRegistry worldRegistry;
    private final ShopInventory shopInventory;
    private HandlerType lastSwitchedTo;

    PlayContextTest(GameState gameState, WorldRegistry worldRegistry, ShopInventory shopInventory) {
        this.gameState = gameState;
        this.worldRegistry = worldRegistry;
        this.shopInventory = shopInventory;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public WorldRegistry getWorldRegistry() {
        return worldRegistry;
    }

    @Override
    public ShopInventory getShopInventory() {
        return shopInventory;
    }

    @Override
    public SimCharacter getActivePlayer() {
        return gameState.getActivePlayer();
    }

    @Override
    public void switchTo(HandlerType type) {
        this.lastSwitchedTo = type;
    }

    HandlerType getLastSwitchedTo() {
        return lastSwitchedTo;
    }
}