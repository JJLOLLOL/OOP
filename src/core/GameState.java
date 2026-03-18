package core;

import java.util.ArrayList;
import java.util.List;
import models.SimCharacter;
import services.RelationshipManager;

/**
 * Holds all mutable runtime state for the game. Passed around as a single
 * source of truth; no static singletons needed.
 */
public class GameState {

    public enum Phase {
        CREATE_SIM, PLAYING, QUIT
    }

    private Phase phase = Phase.CREATE_SIM;

    private final List<SimCharacter> sims = new ArrayList<>();
    private SimCharacter activePlayer;

    private final RelationshipManager relationshipManager = new RelationshipManager();
    private final GameClock gameClock = new GameClock();

    // ── Phase ─────────────────────────────────────────────────────────────────
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public boolean isRunning() {
        return phase != Phase.QUIT;
    }

    // ── Sims ──────────────────────────────────────────────────────────────────
    public List<SimCharacter> getSims() {
        return sims;
    }

    public void addSim(SimCharacter sim) {
        sims.add(sim);
    }

    public SimCharacter getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(SimCharacter player) {
        this.activePlayer = player;
    }

    // ── Services ──────────────────────────────────────────────────────────────
    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public GameClock getGameClock() {
        return gameClock;
    }
}
