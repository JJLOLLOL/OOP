package core;

import models.Location;
import models.SimCharacter;
import ui.GameState;
import ui.InitializationState;

import java.util.Scanner;

public class GameEngine {
    private static GameEngine instance;
    private SimCharacter activePlayer;
    private Location currentLocation;
    private boolean isRunning;
    private GameState activeState;
    private Scanner scanner;

    public GameEngine() {}

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    public SimCharacter getActivePlayer(){
        return activePlayer;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location newLocation) {
        currentLocation = newLocation;
    }

    public void setActivePlayer(SimCharacter character) {
        activePlayer = character;
    }

    public void setGameState(GameState newState) {
        this.currentGameState = newState;
    }

    public void setIsRunning(boolean value) {
        this.isRunning = value;
    }

    public void start() {
        if (!isRunning) {
            setIsRunning(true);
            // Trigger point to start UI, navigating between interfaces handled by UI classes
            setGameState(new InitializationState());
            while (isRunning) {
                activeState.render(this);

                String input = scanner.nextLine().trim();
                activeState.handleInput(input, this);
            }
        } else {
            System.out.println("Game engine failed to start!");
        }
    }

    public void changeState(GameState newState) {
        currentState = newState;
    }

}