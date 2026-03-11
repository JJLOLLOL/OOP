package core;

import models.Location;
import models.SimCharacter;
import ui.states.State;

import java.util.Scanner;

public class GameEngine {
    private static GameEngine instance;
    private SimCharacter activePlayer;
    private Location currentLocation;
    private boolean isRunning;
    private State<?> activeState;
    private final Scanner scanner;

    public GameEngine() {
        this.scanner = new Scanner(System.in);
    }

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

    public void setGameState(State<?> newState) {
        this.activeState = newState;
    }

    public void setIsRunning(boolean value) {
        this.isRunning = value;
    }

    public void start(State<?> initialState) {
        setGameState(initialState);
        run();
    }

    private void run() {
        setIsRunning(true);
        while (isRunning) {
            activeState.render(this);
        }
    }

    public void end(){
        setIsRunning(false);
    }
}