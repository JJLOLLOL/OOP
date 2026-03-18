package services;

import core.GameState;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import models.Location;
import models.SimCharacter;
import ui.Renderer;

/**
 * Handles all input during the CREATE_SIM phase. Drives a simple multi-step
 * form: count → name/age/gender per sim → confirm → pick active player.
 */
public class CreateSimService {

    // ── Per-conversation state (static because only one creation session exists) ──
    private static Step step = Step.COUNT;
    private static int totalSims = 0;
    private static int currentIndex = 0;

    // In-flight fields for the sim being entered
    private static String name = "", age = "", gender = "";

    // Committed sims (stored as raw arrays until all confirmed)
    private static final List<String[]> committed = new ArrayList<>();

    public enum Step {
        COUNT, NAME, AGE, GENDER, CONFIRM, PICK_PLAYER
    }

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void handleInput(String input, GameState state, WorldRegistry world) {
        switch (step) {

            case COUNT -> {
                try {
                    int n = Integer.parseInt(input);
                    if (n < 1) {
                        throw new NumberFormatException();
                    }
                    totalSims = n;
                    currentIndex = 0;
                    committed.clear();
                    setStep(Step.NAME);
                } catch (NumberFormatException e) {
                    Renderer.showError("Please enter a positive number.");
                }
            }

            case NAME -> {
                if (input.isBlank()) {
                    Renderer.showError("Name cannot be empty.");
                    return;
                }
                name = input;
                setStep(Step.AGE);
            }

            case AGE -> {
                try {
                    int a = Integer.parseInt(input);
                    if (a < 1 || a > 120) {
                        throw new NumberFormatException();
                    }
                    age = input;
                    setStep(Step.GENDER);
                } catch (NumberFormatException e) {
                    Renderer.showError("Age must be a number between 1 and 120.");
                }
            }

            case GENDER -> {
                if (input.isBlank()) {
                    Renderer.showError("Gender cannot be empty.");
                    return;
                }
                gender = input;
                committed.add(new String[]{name, age, gender});
                currentIndex++;
                name = "";
                age = "";
                gender = "";

                if (currentIndex < totalSims) {
                    setStep(Step.NAME);
                } else {
                    setStep(Step.CONFIRM);
                }
            }

            case CONFIRM -> {
                switch (input.toLowerCase()) {
                    case "y", "yes" ->
                        finaliseSims(state, world);
                    case "n", "no" -> {
                        // Start over
                        committed.clear();
                        currentIndex = 0;
                        setStep(Step.COUNT);
                    }
                    default ->
                        Renderer.showError("Enter Y to confirm or N to start over.");
                }
            }

            case PICK_PLAYER -> {
                try {
                    int pick = Integer.parseInt(input) - 1;
                    List<SimCharacter> sims = state.getSims();
                    if (pick < 0 || pick >= sims.size()) {
                        throw new NumberFormatException();
                    }
                    state.setActivePlayer(sims.get(pick));
                    state.setPhase(GameState.Phase.PLAYING);
                    Renderer.render(state, world);
                } catch (NumberFormatException e) {
                    Renderer.showError("Enter a number between 1 and " + state.getSims().size() + ".");
                }
            }
        }

        // Re-render after every valid step change
        Renderer.render(state, world);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private static void setStep(Step next) {
        step = next;
    }

    private static void finaliseSims(GameState state, WorldRegistry world) {
        Location home = world.getLocation("Home");
        RelationshipManager rm = state.getRelationshipManager();

        for (String[] data : committed) {
            SimCharacter sim = new SimCharacter(data[0], Integer.parseInt(data[1]), data[2], home);
            rm.registerNewSim(sim, state.getSims(), world.getAllNPCs());
            state.addSim(sim);
        }

        if (state.getSims().size() == 1) {
            state.setActivePlayer(state.getSims().get(0));
            state.setPhase(GameState.Phase.PLAYING);
        } else {
            setStep(Step.PICK_PLAYER);
        }
    }

    // ── Accessors used by Renderer ────────────────────────────────────────────
    public static Step getStep() {
        return step;
    }

    public static int getTotalSims() {
        return totalSims;
    }

    public static int getCurrentIndex() {
        return currentIndex;
    }

    public static List<String[]> getCommitted() {
        return committed;
    }

    public static String getInFlightName() {
        return name;
    }

    public static String getInFlightAge() {
        return age;
    }
}
