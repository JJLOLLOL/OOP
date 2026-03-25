package core;

import java.util.ArrayList;
import java.util.List;

import models.character.SimCharacter;
import models.location.Location;
import ui.Renderer;

/**
 * Handles all player input during the {@link GameState.Phase#CREATE_SIM} phase.
 *
 * <p>
 * Drives a linear multi-step form:
 * <pre>
 *   COUNT → NAME → AGE → GENDER → (repeat for each sim) → CONFIRM → PICK_PLAYER
 * </pre>
 *
 * <p>
 * State is held in static fields because only one creation session can exist
 * per run. The {@link ui.Renderer} reads the accessors at the bottom of this
 * class to know what to display at each step.
 *
 * <p>
 * {@link #handleInput} returns {@code true} when the step advanced so
 * {@link GameEngine} knows to trigger a redraw, and {@code false} when an
 * inline error was shown instead (so the error stays visible).
 */
public class CreateSimController {

    // ── Step enum ─────────────────────────────────────────────────────────────
    /**
     * The sequential steps of the sim-creation wizard. {@link ui.Renderer}
     * switches on this to display the right prompt.
     */
    public enum Step {
        COUNT, NAME, AGE, GENDER, CONFIRM, PICK_PLAYER
    }

    // ── Constants ────────────────────────────────────────────────────────────
    /**
     * Maximum number of Sims that can be created in one session.
     */
    private static final int MAX_SIMS = 5;

    // ── Session state ─────────────────────────────────────────────────────────
    private static Step step = Step.COUNT;
    private static int totalSims = 0;
    private static int currentIndex = 0;

    /**
     * In-flight fields for the sim currently being entered.
     */
    private static String name = "", age = "", gender = "";

    /**
     * Sims confirmed so far, stored as {name, age, gender} string arrays.
     */
    private static final List<String[]> committed = new ArrayList<>();

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
    public static boolean handleInput(String input, GameState state, WorldRegistry world) {
        switch (step) {

            case COUNT -> {
                try {
                    int n = Integer.parseInt(input);
                    if (n < 1 || n > MAX_SIMS) {
                        throw new NumberFormatException();
                    }
                    totalSims = n;
                    currentIndex = 0;
                    committed.clear();
                    setStep(Step.NAME);
                } catch (NumberFormatException e) {
                    Renderer.showError("Enter a number between 1 and " + MAX_SIMS + ".");
                    return false;
                }
            }

            case NAME -> {
                if (input.isBlank()) {
                    Renderer.showError("Name cannot be empty.");
                    return false;
                }
                name = input;
                setStep(Step.AGE);
            }

            case AGE -> {
                try {
                    int a = Integer.parseInt(input);
                    if (a < 10 || a > 90) {
                        throw new NumberFormatException();
                    }
                    age = input;
                    setStep(Step.GENDER);
                } catch (NumberFormatException e) {
                    Renderer.showError("Age must be a number between 10 and 90.");
                    return false;
                }
            }

            case GENDER -> {
                String g = input.trim().toUpperCase();
                if (!g.equals("M") && !g.equals("F")) {
                    Renderer.showError("Enter M for Male or F for Female.");
                    return false;
                }
                gender = g.equals("M") ? "Male" : "Female";
                committed.add(new String[]{name, age, gender});
                name = "";
                age = "";
                gender = "";
                setStep(++currentIndex < totalSims ? Step.NAME : Step.CONFIRM);
            }

            case CONFIRM -> {
                switch (input.toLowerCase()) {
                    case "y", "yes" ->
                        finaliseSims(state, world);
                    case "n", "no" -> {
                        committed.clear();
                        currentIndex = 0;
                        setStep(Step.COUNT);
                    }
                    default -> {
                        Renderer.showError("Enter Y to confirm or N to start over.");
                        return false;
                    }
                }
            }

            case PICK_PLAYER -> {
                try {
                    int pick = Integer.parseInt(input) - 1;
                    if (pick < 0 || pick >= state.getSims().size()) {
                        throw new NumberFormatException();
                    }
                    state.setActivePlayer(state.getSims().get(pick));
                    state.setPhase(GameState.Phase.PLAYING);
                } catch (NumberFormatException e) {
                    Renderer.showError("Enter a number between 1 and " + state.getSims().size() + ".");
                    return false;
                }
            }
        }

        return true;
    }

    // ── Private helpers ───────────────────────────────────────────────────────
    /**
     * Converts all committed sim data into {@link SimCharacter} objects,
     * registers their relationships, and adds them to the game state.
     * All sims are assigned to the shared global Home location from the world.
     * Transitions to {@link Step#PICK_PLAYER} if multiple sims were created, or
     * directly to {@link GameState.Phase#PLAYING} for a single sim.
     */
    private static void finaliseSims(GameState state, WorldRegistry world) {
        Location home = world.getLocation("Home");

        for (String[] data : committed) {
            SimCharacter sim = new SimCharacter(data[0], Integer.parseInt(data[1]), data[2], home);
            state.getRelationshipService().registerNewSim(sim, state.getSims(), world.getAllNPCs());
            state.addSim(sim);
            
            // Assign the shared global Home as the player's house
            sim.assignHouse((models.location.House) home);
        }

        if (state.getSims().size() == 1) {
            state.setActivePlayer(state.getSims().get(0));
            state.setPhase(GameState.Phase.PLAYING);
        } else {
            setStep(Step.PICK_PLAYER);
        }
    }

    private static void setStep(Step next) {
        step = next;
    }

    // ── Accessors for Renderer ────────────────────────────────────────────────
    /**
     * Returns the current wizard step.
     */
    public static Step getStep() {
        return step;
    }

    /**
     * Returns the total number of sims to create this session.
     */
    public static int getTotalSims() {
        return totalSims;
    }

    /**
     * Returns the zero-based index of the sim currently being entered.
     */
    public static int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Returns the list of sims confirmed so far as {@code {name, age, gender}}
     * arrays.
     */
    public static List<String[]> getCommitted() {
        return committed;
    }

    /**
     * Returns the name typed for the sim currently in progress.
     */
    public static String getInFlightName() {
        return name;
    }

    /**
     * Returns the age typed for the sim currently in progress.
     */
    public static String getInFlightAge() {
        return age;
    }
}
