package app;

import core.GameEngine;
import core.WorldRegistry;
import data.DataParser;
import data.WorldData;

/**
 * Application entry point.
 * <p>
 * This class is responsible for initializing the game data by using the
 * {@link DataParser}, constructing the main game components like
 * {@link WorldRegistry} and {@link GameEngine}, and starting the game loop.
 * This decouples the core game engine from the data loading process.
 */
public class Main {
    public static void main(String[] args) {
        DataParser parser = new DataParser();
        WorldData worldData = parser.loadWorldData();

        WorldRegistry world = new WorldRegistry(worldData.getLocations(), worldData.getNpcs());
        new GameEngine(world, worldData.getShopInventory()).start();
    }
}