package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controller.creation.CountStepHandler;
import controller.creation.CreationStepHandler;
import controller.creation.SimCharacterBuilder;
import core.GameEngine;
import core.GameState;
import core.WorldRegistry;
import models.character.SimCharacter;
import models.location.Location;

/**
 * Handles all player input during the {@link GameState.Phase#CREATE_SIM} phase.
 *
 * <p>
 * Drives a linear multi-step form:
 * <pre>
 *   COUNT → NAME → AGE → GENDER → (repeat for each sim) → CONFIRM → PICK_PLAYER
 * </pre>
 *
 * <p>This class acts as a "Context" for the State design pattern. It holds the
 * current step handler ({@link CreationStepHandler}) and the shared data
 * (the builders). It delegates all input processing to the active handler,
 * which in turn calls back to this context to transition to the next step.
 * <p>
 * {@link #handleInput} returns {@code true} when the step advanced so
 * {@link GameEngine} knows to trigger a redraw, and {@code false} when an
 * inline error was shown instead (so the error stays visible).
 */
public class CreateSimController {

    /**
     * The sequential steps of the sim-creation wizard. {@link ui.Renderer}
     * switches on this to display the right prompt.
     */
    public enum Step {
        COUNT, NAME, AGE, GENDER, CONFIRM, PICK_PLAYER
    }

    // ── Session state ─────────────────────────────────────────────────────────
    private CreationStepHandler currentHandler;
    private int totalSims = 0;
    private int currentIndex = 0;

    /**
     * A list of builders, one for each Sim being created.
     */
    private final List<SimCharacterBuilder> builders = new ArrayList<>();

    /**
     * Transient references to state and world, set for the duration of a handleInput call.
     */
    private GameState gameState;
    private WorldRegistry worldRegistry;

    public CreateSimController() {
        this.currentHandler = new CountStepHandler();
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    /**
     * Processes one line of player input for the current wizard step.
     *
     * @param input the trimmed player input line
     * @param state the live game state
     * @param world the world registry
     * @return {@code true} if the step advanced and the screen should redraw;
     * {@code false} if an inline error was shown and the screen should remain
     * as-is so the player can read it
     */
    public boolean handleInput(String input, GameState state, WorldRegistry world) {
        this.gameState = state;
        this.worldRegistry = world;
        return currentHandler.handleInput(input, this);
    }

    // ── Private helpers ───────────────────────────────────────────────────────
    /**
     * Converts all committed sim data into {@link SimCharacter} objects,
     * registers their relationships, and adds them to the game state.
     * All sims are assigned to the shared global Home location from the world.
     * Transitions to {@link Step#PICK_PLAYER} if multiple sims were created, or
     * directly to {@link GameState.Phase#PLAYING} for a single sim.
     */
    public void finaliseSims(GameState state, WorldRegistry world) {
        Location home = world.getLocation("Home");
        for (SimCharacterBuilder builder : builders) {
            SimCharacter sim = builder.build(home);
            state.getRelationshipService().registerNewSim(sim, state.getSims(), world.getAllNPCs());
            state.addSim(sim);
            sim.assignHouse((models.location.House) home);
        }

        if (state.getSims().size() == 1) {
            state.setActivePlayer(state.getSims().get(0));
            state.setPhase(GameState.Phase.PLAYING);
        }
    }

    // ── Methods for Handlers (Context API) ────────────────────────────────────

    public void setStepHandler(CreationStepHandler handler) {
        this.currentHandler = handler;
    }

    public void initializeBuilders(int count) {
        this.totalSims = count;
        this.currentIndex = 0;
        this.builders.clear();
        for (int i = 0; i < count; i++) {
            this.builders.add(new SimCharacterBuilder());
        }
    }

    public void resetCreation() {
        this.totalSims = 0;
        this.currentIndex = 0;
        this.builders.clear();
    }

    public SimCharacterBuilder getCurrentBuilder() {
        return builders.get(currentIndex);
    }

    public void advanceToNextBuilder() {
        currentIndex++;
    }

    public boolean isCreationFinished() {
        return currentIndex >= totalSims;
    }

    public GameState getGameState() {
        return gameState;
    }

    public WorldRegistry getWorldRegistry() {
        return worldRegistry;
    }

    // ── Accessors for Renderer ────────────────────────────────────────────────
    /**
     * Returns the current wizard step.
     */
    public Step getStep() {
        return currentHandler.getStep();
    }

    /**
     * Returns the total number of sims to create this session.
     */
    public int getTotalSims() {
        return totalSims;
    }

    /**
     * Returns the zero-based index of the sim currently being entered.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Returns the list of builders for sims confirmed so far.
     */
    public List<SimCharacterBuilder> getCommitted() {
        return Collections.unmodifiableList(builders);
    }

    /**
     * Returns the name typed for the sim currently in progress.
     */
    public String getInFlightName() {
        if (currentIndex < totalSims) {
            String name = getCurrentBuilder().getName();
            return name != null ? name : "";
        }
        return "";
    }

    /**
     * Returns the age typed for the sim currently in progress.
     */
    public String getInFlightAge() {
        if (currentIndex < totalSims) {
            int age = getCurrentBuilder().getAge();
            return age > 0 ? String.valueOf(age) : "";
        }
        return "";
    }
}