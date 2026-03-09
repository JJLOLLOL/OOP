package core;

import models.SimCharacter;
import ui.GameState;

public class GameEngine {
    private static GameEngine instance;
    private SimCharacter activePlayer;
    private boolean isRunning;
    private GameState activeState;

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
            }
        } else {
            System.out.println("Game engine failed to start!");
        }
    }

    public void end(){
        setIsRunning(false);
    }
}