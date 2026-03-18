package core;

import java.util.Scanner;
import services.*;
import ui.Renderer;

/**
 * Owns the game loop. Drives ticks, delegates update/render to services and
 * Renderer.
 */
public class GameEngine {

    private final GameState state;
    private final WorldRegistry world;
    private final NpcService npcService;

    private final InputQueue inputQueue;
    private final InputThread inputThread;
    private final Thread inputThreadHandle;

    public GameEngine() {
        this.state = new GameState();
        this.world = new WorldRegistry();
        this.npcService = new NpcService(world);

        this.inputQueue = new InputQueue();
        this.inputThread = new InputThread(new Scanner(System.in), this.inputQueue);
        this.inputThreadHandle = new Thread(this.inputThread, "Input-Thread");
        this.inputThreadHandle.setDaemon(true);
    }

    public GameState getState() {
        return state;
    }

    public WorldRegistry getWorld() {
        return world;
    }

    public String pollInput() {
        return inputQueue.poll();
    }

    public void start() {
        inputThreadHandle.start();
        run();
    }

    // ── Game loop ─────────────────────────────────────────────────────────────
    private void run() {
        final double UPDATES_PER_SECOND = 20.0;
        final double NS_PER_UPDATE = 1_000_000_000.0 / UPDATES_PER_SECOND;

        // Re-render the playing screen once per real second so the clock visually ticks
        final long NS_PER_RENDER = 1_000_000_000L;

        long lastTime = System.nanoTime();
        double unprocessed = 0;
        long lastRenderTime = System.nanoTime();

        // Initial render (shows the create-sim prompt)
        Renderer.render(state, world);

        while (state.isRunning()) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime);
            lastTime = now;

            // Fixed-timestep logic ticks
            while (unprocessed >= NS_PER_UPDATE) {
                unprocessed -= NS_PER_UPDATE;
                tick(1.0 / UPDATES_PER_SECOND);
            }

            // Check for player input — always re-render after handling it
            String input = pollInput();
            if (input != null) {
                handleInput(input.trim());
                lastRenderTime = now; // reset cadence to avoid an immediate double-draw
            } else if (state.getPhase() == GameState.Phase.PLAYING
                    && (now - lastRenderTime) >= NS_PER_RENDER) {
                // Periodic redraw during gameplay keeps the clock and needs bar fresh
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
    private void tick(double dt) {
        state.getGameClock().tick(dt);

        // Update every sim's needs and expire old notifications
        for (models.SimCharacter sim : state.getSims()) {
            sim.updateNeed(dt / 60.0); // /60 so decay is per real-minute not per real-second
            sim.tickNotifications();   // expire notifications older than 8 real seconds
        }

        // Move NPCs to wherever their schedule says they should be right now
        npcService.updateNPCLocations(state.getGameClock());
    }

    // ── Input routing ─────────────────────────────────────────────────────────
    private void handleInput(String input) {
        switch (state.getPhase()) {
            case CREATE_SIM ->
                CreateSimService.handleInput(input, state, world);
            case PLAYING ->
                PlayService.handleInput(input, state, world);
            default -> {
            }
        }
    }

    // ── Shutdown ──────────────────────────────────────────────────────────────
    private void shutdown() {
        System.out.println("\nGame over. Thanks for playing!");
        inputThread.stop();
    }
}
