package app;

import core.GameEngine;
import core.WorldRegistry;
import data.WorldLoader;
import data.WorldData;

/**
 * Application entry point.
 * <p>
 * This class is responsible for initializing the game data by using the
 * {@link WorldLoader}, constructing the main game components like
 * {@link WorldRegistry} and {@link GameEngine}, and starting the game loop.
 * This decouples the core game engine from the data loading process.
 */
public class Main {
    /**
     * Loads world data, constructs the runtime services, and starts the game.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        WorldLoader parser = new WorldLoader();
        WorldData worldData = parser.loadWorldData();

        WorldRegistry world = new WorldRegistry(worldData.getLocations(), worldData.getNpcs());
        new GameEngine(world, worldData.getShopInventory()).start();
    }
}
