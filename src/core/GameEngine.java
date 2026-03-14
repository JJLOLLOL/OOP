package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Location;
import models.SimCharacter;
import services.RelationshipManager;
import ui.state.State;

public class GameEngine {

    private static GameEngine instance;
    private ArrayList<SimCharacter> sims = new ArrayList<>();
    private SimCharacter activePlayer;
    private RelationshipManager relationshipManager = new RelationshipManager();
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

    public Scanner getScanner() {
        return scanner;
    }

    public SimCharacter getActivePlayer() {
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

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public List<SimCharacter> getSims() {
        return sims;
    }
    
    public void addSim(SimCharacter sim) {
        sims.add(sim);
    }

    public void setGameState(State<?> newState) {
        this.activeState = newState;
    }

    public void setIsRunning(boolean value) {
        this.isRunning = value;
    }

    public void start(State<?> initialState) {
        setGameState(initialState);
        WorldRegistry.getInstance();
        run();
    }

    private void run() {
        setIsRunning(true);
        long lastTime = System.nanoTime();
        while (isRunning) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1000000000.0;
            lastTime = now;
            activeState.update(this, deltaTime);
            activeState.render(this);
        }
    }

    public void end() {
        setIsRunning(false);
        scanner.close();
    }
}
