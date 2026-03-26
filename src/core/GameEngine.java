package core;

import java.util.Scanner;

import controller.CreateSimController;
import controller.PlayController;
import data.DataParser;
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
     * Number of UI renders per real second.
     */
    private static final double RENDERS_PER_SECOND = 1.0;

    /**
     * Nanoseconds between each UI render.
     */
    private static final long NS_PER_RENDER = (long) (1_000_000_000.0 / RENDERS_PER_SECOND);

    // ── Fields ────────────────────────────────────────────────────────────────
    private final GameState state;
    private final WorldRegistry world;
    private final NpcService npcService;
    private final CreateSimController createSimController;
    private final PlayController playController;

    private final InputQueue inputQueue;
    private final InputThread inputThread;
    private final Thread inputThreadHandle;

    // ── Constructor ───────────────────────────────────────────────────────────
    public GameEngine(WorldRegistry world, data.ShopInventory shopInventory) {
        this.state = new GameState();
        this.world = world;
        this.npcService = new NpcService(world);
        this.createSimController = new CreateSimController();
        this.playController = new PlayController(shopInventory);

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
        double unprocessed = 0;
        long lastRenderTime = System.nanoTime(); // Track last render time for periodic updates
        Renderer.render(state, world, createSimController, playController); // show the initial create-sim screen

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
            boolean inputCausedRender = false;
            if (input != null) {
                inputCausedRender = handleInput(input.trim());
            }

            // Render periodically during PLAYING phase, or immediately if input caused a state change
            // In CREATE_SIM phase, only render if inputCausedRender is true.
            if (inputCausedRender || (state.getPhase() == GameState.Phase.PLAYING && (now - lastRenderTime >= NS_PER_RENDER))) {
                Renderer.render(state, world, createSimController, playController);
                lastRenderTime = now;
            }

            // Sleep to prevent busy-waiting and reduce CPU usage
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
        if (state.getPhase() == GameState.Phase.PLAYING) {
            state.getGameClock().tick(dt);

            for (models.character.SimCharacter sim : state.getSims()) {
                sim.updateNeeds(dt / 60.0);
                services.NotificationService.tick(sim); // Tick notifications for all Sims
            }

            npcService.updateNPCLocations(state.getGameClock());
        }
    }

    // ── Input routing ─────────────────────────────────────────────────────────
    /**
     * Forwards trimmed player input to the active phase controller. Only
     * re-renders if the controller reports that the step changed. If the
     * controller showed an inline error instead, the render is skipped so the
     * error remains visible until the player types their next input.
     *
     * @return {@code true} if the UI needs to be re-rendered due to a state change, {@code false} otherwise.
     */
    private boolean handleInput(String input) { // Changed return type to boolean
        return switch (state.getPhase()) {
            case CREATE_SIM -> createSimController.handleInput(input, state, world);
            case PLAYING -> playController.handleInput(input, state, world);
            default -> false; // Should not happen if state.isRunning() is true
        };
    }

    // ── Shutdown ──────────────────────────────────────────────────────────────
    private void shutdown() {
        System.out.println("\nGame over. Thanks for playing!");
        inputThread.stop();
    }
}