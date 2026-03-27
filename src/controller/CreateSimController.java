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

public class CreateSimController {

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

    public CreateSimController() {
        this.currentHandler = new CountStepHandler();
    }

    public boolean handleInput(String input, GameState state, WorldRegistry world) {
        this.gameState = state;
        this.worldRegistry = world;
        return currentHandler.handleInput(input, this);
    }

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

    public String getInFlightName() {
        if (currentIndex < totalSims) {
            String name = getCurrentBuilder().getName();
            return name != null ? name : "";
        }
        return "";
    }

    public String getInFlightAge() {
        if (currentIndex < totalSims) {
            int age = getCurrentBuilder().getAge();
            return age > 0 ? String.valueOf(age) : "";
        }
        return "";
    }
}