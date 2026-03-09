package core;

import models.Location;
import models.SimCharacter;
import ui.GameState;

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
        this.activeState = newState;
    }

    public void setIsRunning(boolean value) {
        this.isRunning = value;
    }

    public void start(GameState initialState) {
        setGameState(initialState);
        run();
    }

    private void run() {
        if (!isRunning) {
            setIsRunning(true);
            while (isRunning) {
                activeState.render(this);

                String input = scanner.nextLine().trim();
                activeState.handleInput(input, this);
            }
        } else {
            System.out.println("Game engine failed to start!");
        }
    }

    public void end(){
        setIsRunning(false);
    }
}