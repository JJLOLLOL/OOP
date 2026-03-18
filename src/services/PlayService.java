package services;

import Types.InteractionType;
import core.GameState;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import models.Location;
import models.SimCharacter;
import models.furnitureactions.Furniture;
import ui.Renderer;

/**
 * Handles all player input while the game is in the PLAYING phase. Drives a
 * simple numbered-menu flow via a step enum.
 */
public class PlayService {

    public enum Step {
        MAIN,
        INTERACTABLES, INTERACTABLE_ACTION,
        SOCIALISE, SOCIALISE_ACTION,
        CHANGE_LOCATION,
        SWITCH_CHARACTER
    }

    private static Step step = Step.MAIN;
    private static Furniture selectedFurniture = null;
    private static models.Character selectedCharacter = null;

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void handleInput(String input, GameState state, WorldRegistry world) {
        SimCharacter player = state.getActivePlayer();
        Location loc = player.getLocation();

        switch (step) {

            case MAIN -> {
                switch (input) {
                    case "1" ->
                        setStep(Step.INTERACTABLES);
                    case "2" ->
                        setStep(Step.SOCIALISE);
                    case "3" ->
                        setStep(Step.CHANGE_LOCATION);
                    case "4" ->
                        setStep(Step.SWITCH_CHARACTER);
                    case "5" ->
                        state.setPhase(GameState.Phase.QUIT);
                    default ->
                        Renderer.showError("Invalid choice. Enter 1-5.");
                }
            }

            case INTERACTABLES -> {
                if (input.equals("0")) {
                    setStep(Step.MAIN);
                } else {
                    List<Furniture> furnitures = loc.getFurnitures();
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx < 0 || idx >= furnitures.size()) {
                            throw new NumberFormatException();
                        }
                        selectedFurniture = furnitures.get(idx);
                        setStep(Step.INTERACTABLE_ACTION);
                    } catch (NumberFormatException e) {
                        Renderer.showError("Enter a number from the list, or 0 to go back.");
                    }
                }
            }

            case INTERACTABLE_ACTION -> {
                if (input.equals("0")) {
                    selectedFurniture = null;
                    setStep(Step.INTERACTABLES);
                } else {
                    List<String> actions = new ArrayList<>(selectedFurniture.getActionNames());
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx < 0 || idx >= actions.size()) {
                            throw new NumberFormatException();
                        }
                        boolean ok = selectedFurniture.performAction(actions.get(idx), player);
                        if (!ok) {
                            player.addNotification("Action failed: not enough money or needs too low.");
                        }
                        selectedFurniture = null;
                        setStep(Step.MAIN);
                    } catch (NumberFormatException e) {
                        Renderer.showError("Enter a number from the list, or 0 to go back.");
                    }
                }
            }

            case SOCIALISE -> {
                if (input.equals("0")) {
                    setStep(Step.MAIN);
                } else {
                    List<models.Character> chars = charsAt(loc, state, world);
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx < 0 || idx >= chars.size()) {
                            throw new NumberFormatException();
                        }
                        selectedCharacter = chars.get(idx);
                        setStep(Step.SOCIALISE_ACTION);
                    } catch (NumberFormatException e) {
                        Renderer.showError("Enter a number from the list, or 0 to go back.");
                    }
                }
            }

            case SOCIALISE_ACTION -> {
                if (input.equals("0")) {
                    selectedCharacter = null;
                    setStep(Step.SOCIALISE);
                } else {
                    InteractionType[] types = InteractionType.values();
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx < 0 || idx >= types.length) {
                            throw new NumberFormatException();
                        }
                        InteractionType chosen = types[idx];
                        String result = state.getRelationshipManager().interact(player, selectedCharacter, chosen);
                        player.adjustNeed("Social", chosen.getValue());
                        player.addNotification(result);
                        selectedCharacter = null;
                        setStep(Step.MAIN);
                    } catch (NumberFormatException e) {
                        Renderer.showError("Enter a number from the list, or 0 to go back.");
                    }
                }
            }

            case CHANGE_LOCATION -> {
                if (input.equals("0")) {
                    setStep(Step.MAIN);
                } else {
                    List<Location> locs = new ArrayList<>(world.getAllLocations());
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx < 0 || idx >= locs.size()) {
                            throw new NumberFormatException();
                        }
                        player.setLocation(locs.get(idx));
                        setStep(Step.MAIN);
                    } catch (NumberFormatException e) {
                        Renderer.showError("Enter a number from the list, or 0 to go back.");
                    }
                }
            }

            case SWITCH_CHARACTER -> {
                if (input.equals("0")) {
                    setStep(Step.MAIN);
                } else {
                    List<SimCharacter> sims = state.getSims();
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx < 0 || idx >= sims.size()) {
                            throw new NumberFormatException();
                        }
                        state.setActivePlayer(sims.get(idx));
                        setStep(Step.MAIN);
                    } catch (NumberFormatException e) {
                        Renderer.showError("Enter a number from the list, or 0 to go back.");
                    }
                }
            }
        }
    }

    // ── Accessors for Renderer ────────────────────────────────────────────────
    public static Step getStep() {
        return step;
    }

    public static Furniture getSelectedFurniture() {
        return selectedFurniture;
    }

    public static models.Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public static List<models.Character> charsAt(Location loc, GameState state, WorldRegistry world) {
        SimCharacter player = state.getActivePlayer();
        List<models.Character> chars = new ArrayList<>();
        state.getSims().stream()
                .filter(s -> !s.equals(player) && s.getLocation().equals(loc))
                .forEach(chars::add);
        world.getAllNPCs().stream()
                .filter(n -> n.getLocation().equals(loc))
                .forEach(chars::add);
        return chars;
    }

    // ── Private helpers ───────────────────────────────────────────────────────
    private static void setStep(Step next) {
        step = next;
    }
}
