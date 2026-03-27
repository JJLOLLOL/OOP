package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controller.creation.CountStepHandler;
import controller.creation.CreationStepHandler;
import controller.creation.SimCharacterBuilder;
import core.GameState;
import core.WorldRegistry;
import models.character.SimCharacter;
import models.location.Location;

/**
 * Coordinates the multi-step Sim creation flow and stores in-progress builder
 * state between prompts.
 */
public class CreateSimController {

    /**
     * Steps shown during the create-sim wizard.
     */
    public enum Step {
        COUNT, NAME, AGE, GENDER, CONFIRM, PICK_PLAYER
    }

    // ── Session state ─────────────────────────────────────────────────────────
    private CreationStepHandler currentHandler;
    private int totalSims = 0;
    private int currentIndex = 0;

    private final List<SimCharacterBuilder> builders = new ArrayList<>();
    private GameState gameState;
    private WorldRegistry worldRegistry;

    /**
     * Creates a controller positioned at the initial count-selection step.
     */
    public CreateSimController() {
        this.currentHandler = new CountStepHandler();
    }

    /**
     * Delegates raw input to the active step handler after refreshing the
     * controller context.
     *
     * @param input the player's raw input
     * @param state the shared game state for this creation session
     * @param world the loaded world registry
     * @return {@code true} when the UI should redraw, {@code false} when an
     * inline error was shown
     */
    public boolean handleInput(String input, GameState state, WorldRegistry world) {
        this.gameState = state;
        this.worldRegistry = world;
        return currentHandler.handleInput(input, this);
    }

    /**
     * Materializes all configured builders into playable sims and injects them
     * into the active game state.
     *
     * @param state the game state receiving the finished sims
     * @param world the world registry used to resolve the shared home location
     */
    public void finaliseSims(GameState state, WorldRegistry world) {
        Location home = world.getLocation("Home");
        if (home == null) {
            throw new IllegalStateException("Required location 'Home' was not found.");
        }
        if (!(home instanceof models.location.House house)) {
            throw new IllegalStateException("'Home' must be a House.");
        }
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

    /**
     * Replaces the active step handler.
     *
     * @param handler the handler that should control the next prompt
     */
    public void setStepHandler(CreationStepHandler handler) {
        this.currentHandler = handler;
    }

    /**
     * Resets the builder list for a new creation batch of the requested size.
     *
     * @param count the number of sims to create
     */
    public void initializeBuilders(int count) {
        this.totalSims = count;
        this.currentIndex = 0;
        this.builders.clear();
        for (int i = 0; i < count; i++) {
            this.builders.add(new SimCharacterBuilder());
        }
    }

    /**
     * Clears all create-sim progress and returns to an empty session.
     */
    public void resetCreation() {
        this.totalSims = 0;
        this.currentIndex = 0;
        this.builders.clear();
    }

    public SimCharacterBuilder getCurrentBuilder() {
        return builders.get(currentIndex);
    }

    /**
     * Advances the controller to the next builder slot.
     */
    public void advanceToNextBuilder() {
        currentIndex++;
    }

    /**
     * Returns whether every requested builder has already been filled in.
     *
     * @return {@code true} when no more builders remain
     */
    public boolean isCreationFinished() {
        return currentIndex >= totalSims;
    }

    public GameState getGameState() {
        return gameState;
    }

    public WorldRegistry getWorldRegistry() {
        return worldRegistry;
    }

    public Step getStep() {
        return currentHandler.getStep();
    }

    public int getTotalSims() {
        return totalSims;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public List<SimCharacterBuilder> getCommitted() {
        return Collections.unmodifiableList(builders);
    }

    /**
     * Returns the current builder's in-progress name for UI preview purposes.
     *
     * @return the staged name, or an empty string when unavailable
     */
    public String getInFlightName() {
        if (currentIndex < totalSims) {
            String name = getCurrentBuilder().getName();
            return name != null ? name : "";
        }
        return "";
    }

    /**
     * Returns the current builder's in-progress age for UI preview purposes.
     *
     * @return the staged age as text, or an empty string when unavailable
     */
    public String getInFlightAge() {
        if (currentIndex < totalSims) {
            int age = getCurrentBuilder().getAge();
            return age > 0 ? String.valueOf(age) : "";
        }
        return "";
    }
}
