package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.character.SimCharacter;
import services.AchievementService;
import services.RelationshipService;

/**
 * Single source of truth for all mutable runtime state.
 *
 * <p>
 * Passed by reference throughout the codebase so every component reads and
 * writes the same instance. No static singletons needed.
 *
 * <p>
 * Owns the services that live for the full duration of the game:
 * {@link RelationshipService} and {@link AchievementService}.
 */
public class GameState {

    /**
     * The three top-level phases the game can be in at any time.
     */
    public enum Phase {
        CREATE_SIM, PLAYING, QUIT
    }

    private Phase phase = Phase.CREATE_SIM;

    private final List<SimCharacter> sims = new ArrayList<>();
    private SimCharacter activePlayer;

    private final RelationshipService relationshipService = new RelationshipService();
    private final AchievementService achievementService = new AchievementService();
    private final GameClock gameClock = new GameClock();

    // ── Phase ─────────────────────────────────────────────────────────────────
    /**
     * Returns the current game phase.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Sets the current game phase.
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * Returns {@code true} while the game has not reached {@link Phase#QUIT}.
     */
    public boolean isRunning() {
        return phase != Phase.QUIT;
    }

    // ── Sims ──────────────────────────────────────────────────────────────────
    public void addSim(SimCharacter sim) {
        if (sim == null) {
            throw new IllegalArgumentException("Sim cannot be null.");
        }
        sims.add(sim);
    }

    public void setActivePlayer(SimCharacter player) {
        if (player == null) {
            throw new IllegalArgumentException("Active player cannot be null.");
        }
        this.activePlayer = player;
    }

    public List<SimCharacter> getSims() {
        return Collections.unmodifiableList(sims);
    }
    /**
     * Returns the sim currently controlled by the player.
     */
    public SimCharacter getActivePlayer() {
        return activePlayer;
    }
    // ── Services ──────────────────────────────────────────────────────────────
    /**
     * Returns the shared {@link RelationshipService} instance.
     */
    public RelationshipService getRelationshipService() {
        return relationshipService;
    }

    /**
     * Returns the shared {@link AchievementService} instance.
     */
    public AchievementService getAchievementService() {
        return achievementService;
    }

    /**
     * Returns the in-game clock.
     */
    public GameClock getGameClock() {
        return gameClock;
    }
}
