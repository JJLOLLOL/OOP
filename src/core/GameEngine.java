package core;

import java.util.Scanner;
import services.NpcService;
import ui.Renderer;

/**
 * Owns the game loop and wires the major subsystems together.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Starts the background input thread so {@link System#in} blocking never
 * stalls the main loop.</li>
 * <li>Runs a fixed-timestep tick at 20 Hz for deterministic game logic.</li>
 * <li>Re-renders the playing screen at 1 Hz so the clock stays visually fresh
 * without spamming the terminal.</li>
 * <li>Routes player input to the correct controller based on
 * {@link GameState.Phase}.</li>
 * </ul>
 */
public class GameEngine {

    // ── Constants ─────────────────────────────────────────────────────────────
    /**
     * Number of logic updates per real second.
     */
    private static final double UPDATES_PER_SECOND = 20.0;

    /**
     * Nanoseconds between each logic tick.
     */
    private static final double NS_PER_UPDATE = 1_000_000_000.0 / UPDATES_PER_SECOND;

    /**
     * Nanoseconds between periodic screen redraws during gameplay (1 per
     * second).
     */
    private static final long NS_PER_RENDER = 1_000_000_000L;

    // ── Fields ────────────────────────────────────────────────────────────────
    private final GameState state;
    private final WorldRegistry world;
    private final NpcService npcService;

    private final InputQueue inputQueue;
    private final InputThread inputThread;
    private final Thread inputThreadHandle;

    // ── Constructor ───────────────────────────────────────────────────────────
    public GameEngine() {
        this.state = new GameState();
        this.world = new WorldRegistry();
        this.npcService = new NpcService(world);

        this.inputQueue = new InputQueue();
        this.inputThread = new InputThread(new Scanner(System.in), inputQueue);
        this.inputThreadHandle = new Thread(inputThread, "Input-Thread");
        this.inputThreadHandle.setDaemon(true); // JVM can exit even if this thread is alive
    }

    // ── Public entry point ────────────────────────────────────────────────────
    /**
     * Starts the input thread and enters the main game loop. Blocks until the
     * player quits.
     */
    public void start() {
        inputThreadHandle.start();
        run();
    }

    // ── Game loop ─────────────────────────────────────────────────────────────
    private void run() {
        long lastTime = System.nanoTime();
        long lastRenderTime = System.nanoTime();
        double unprocessed = 0;

        Renderer.render(state, world); // show the initial create-sim screen

        while (state.isRunning()) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime);
            lastTime = now;

            // Consume accumulated time in fixed-size tick increments
            while (unprocessed >= NS_PER_UPDATE) {
                unprocessed -= NS_PER_UPDATE;
                tick(1.0 / UPDATES_PER_SECOND);
            }

            String input = inputQueue.poll();
            if (input != null) {
                handleInput(input.trim());
                lastRenderTime = now; // reset so we don't immediately double-draw
            } else if (state.getPhase() == GameState.Phase.PLAYING
                    && (now - lastRenderTime) >= NS_PER_RENDER) {
                Renderer.render(state, world);
                lastRenderTime = now;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                state.setPhase(GameState.Phase.QUIT);
            }
        }

        shutdown();
    }

    // ── Per-tick logic ────────────────────────────────────────────────────────
    /**
     * Called 20 times per real second. Advances the clock, decays needs,
     * expires notifications, and updates NPC positions.
     *
     * @param dt seconds elapsed this tick (always
     * {@code 1.0 / UPDATES_PER_SECOND})
     */
    private void tick(double dt) {
        state.getGameClock().tick(dt);

        for (models.SimCharacter sim : state.getSims()) {
            // Divide by 60 so need decay rates are expressed per real-minute
            sim.updateNeed(dt / 60.0);
            // Remove notifications that have been on screen for a period of time
            sim.tickNotifications();
        }

        npcService.updateNPCLocations(state.getGameClock());
    }

    // ── Input routing ─────────────────────────────────────────────────────────
    /**
     * Forwards trimmed player input to the active phase controller. Only
     * re-renders if the controller reports that the step changed. If the
     * controller showed an inline error instead, the render is skipped so the
     * error remains visible until the player types their next input.
     */
    private void handleInput(String input) {
        boolean changed = switch (state.getPhase()) {
            case CREATE_SIM ->
                CreateSimController.handleInput(input, state, world);
            case PLAYING ->
                PlayController.handleInput(input, state, world);
            default ->
                false;
        };
        if (changed) {
            Renderer.render(state, world);
        }
    }

    // ── Shutdown ──────────────────────────────────────────────────────────────
    private void shutdown() {
        System.out.println("\nGame over. Thanks for playing!");
        inputThread.stop();
    }
}
