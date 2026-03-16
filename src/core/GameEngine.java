package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Location;
import models.SimCharacter;
import services.RelationshipManager;
import ui.state.State;

public class GameEngine {

    private final ArrayList<SimCharacter> sims = new ArrayList<>();
    private SimCharacter activePlayer;
    private final RelationshipManager relationshipManager = new RelationshipManager();
    private boolean isRunning;
    private State<?> activeState;

    // The engine now owns its input pipeline
    private final InputQueue inputQueue;
    private final InputThread inputThread;
    private final Thread inputThreadHandle;

    public GameEngine() {
        this.inputQueue = new InputQueue();
        this.inputThread = new InputThread(new Scanner(System.in), this.inputQueue);
        this.inputThreadHandle = new Thread(this.inputThread, "Input-Thread");
        this.inputThreadHandle.setDaemon(true); // The JVM can exit even if this thread is running
    }

    public SimCharacter getActivePlayer() {
        return activePlayer;
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

    /**
     * Allows states to poll for user input in a decoupled way.
     */
    public String pollInput() {
        return inputQueue.poll();
    }

    public void start(State<?> initialState) {
        setGameState(initialState);
        WorldRegistry.getInstance(); // Initialize the world data
        inputThreadHandle.start();
        run();
    }

    private void run() {
        isRunning = true;

        final double NANO_SECONDS_PER_SECOND = 1_000_000_000.0;
        final double UPDATES_PER_SECOND = 20.0; // Target updates per second for game logic
        final double NANO_SECONDS_PER_UPDATE = NANO_SECONDS_PER_SECOND / UPDATES_PER_SECOND;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            long now = System.nanoTime();
            unprocessedTime += (now - lastTime);
            lastTime = now;

            // Process updates in a fixed timestep to ensure deterministic game logic
            while (unprocessedTime >= NANO_SECONDS_PER_UPDATE) {
                unprocessedTime -= NANO_SECONDS_PER_UPDATE;
                activeState.update(this, 1.0 / UPDATES_PER_SECOND);
            }

            // Render as fast as possible (or with a frame cap)
            activeState.render(this);

            // Yield to other threads to avoid busy-waiting and save CPU
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                end(); // Exit gracefully if the main thread is interrupted
            }
        }
        shutdown();
    }

    public void end() {
        isRunning = false;
    }

    private void shutdown() {
        System.out.println("Shutting down...");
        inputThread.stop();
        // No need to close System.in scanner, let the JVM handle it.
    }
}
