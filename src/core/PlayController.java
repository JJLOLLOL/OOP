package core;

import Types.InteractionType;
import java.util.ArrayList;
import java.util.List;
import models.Location;
import models.SimCharacter;
import models.actions.Furniture;
import ui.Renderer;

/**
 * Handles all player input during the {@link GameState.Phase#PLAYING} phase.
 *
 * <p>
 * Drives a numbered-menu flow through a {@link Step} enum. The current step
 * determines what the {@link ui.Renderer} displays in the middle panel and how
 * input is interpreted.
 *
 * <p>
 * State is held in static fields because only one play session exists per run.
 *
 * <p>
 * {@link #handleInput} returns {@code true} when the step changed so
 * {@link GameEngine} knows to trigger a redraw, and {@code false} when an
 * inline error was shown instead (so the error stays visible).
 */
public class PlayController {

    // ── Step enum ─────────────────────────────────────────────────────────────
    /**
     * The active sub-menu within the playing phase. {@link ui.Renderer}
     * switches on this to display the correct action list.
     */
    public enum Step {
        MAIN,
        INTERACTABLES, INTERACTABLE_ACTION,
        SOCIALISE, SOCIALISE_ACTION,
        CHANGE_LOCATION,
        SWITCH_CHARACTER
    }

    // ── Session state ─────────────────────────────────────────────────────────
    private static Step step = Step.MAIN;
    private static Furniture selectedFurniture = null;
    private static models.Character selectedCharacter = null;

    // ── Entry point ───────────────────────────────────────────────────────────
    /**
     * Processes one line of player input for the current menu step.
     *
     * @param input the trimmed player input line
     * @param state the live game state
     * @param world the world registry
     * @return {@code true} if the step changed and the screen should redraw;
     * {@code false} if an inline error was shown and the screen should remain
     * as-is so the player can read it
     */
    public static boolean handleInput(String input, GameState state, WorldRegistry world) {
        SimCharacter player = state.getActivePlayer();
        Location loc = player.getLocation();

        return switch (step) {
            case MAIN ->
                handleMain(input, state);
            case INTERACTABLES ->
                handleInteractables(input, loc);
            case INTERACTABLE_ACTION ->
                handleInteractableAction(input, player);
            case SOCIALISE ->
                handleSocialise(input, loc, state, world);
            case SOCIALISE_ACTION ->
                handleSocialiseAction(input, player, state);
            case CHANGE_LOCATION ->
                handleChangeLocation(input, player, world);
            case SWITCH_CHARACTER ->
                handleSwitchCharacter(input, state);
        };
    }

    // ── Sub-handlers ──────────────────────────────────────────────────────────
    /**
     * Main menu: routes to the chosen sub-menu or quits. Options 1–5 map to
     * Interactables, Socialise, Change Location, Switch Character, and Exit.
     */
    private static boolean handleMain(String input, GameState state) {
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
            default -> {
                Renderer.showError("Invalid choice. Enter 1-5.");
                return false;
            }
        }
        return true;
    }

    /**
     * Interactables list: selects a piece of furniture by number. Input
     * {@code "0"} returns to the main menu.
     */
    private static boolean handleInteractables(String input, Location loc) {
        if (input.equals("0")) {
            setStep(Step.MAIN);
            return true;
        }
        return pickFromList(input, loc.getFurnitures(), idx -> {
            selectedFurniture = loc.getFurnitures().get(idx);
            setStep(Step.INTERACTABLE_ACTION);
        });
    }

    /**
     * Furniture action: performs the chosen action on the selected furniture.
     * Notifies the player if the action fails (but still returns to main menu).
     * Input {@code "0"} goes back to the furniture list.
     */
    private static boolean handleInteractableAction(String input, SimCharacter player) {
        if (input.equals("0")) {
            selectedFurniture = null;
            setStep(Step.INTERACTABLES);
            return true;
        }
        List<String> actions = new ArrayList<>(selectedFurniture.getActionNames());
        return pickFromList(input, actions, idx -> {
            boolean ok = selectedFurniture.performAction(actions.get(idx), player);
            if (!ok) {
                player.addNotification("Action failed: not enough money or needs too low.");
            }
            selectedFurniture = null;
            setStep(Step.MAIN);
        });
    }

    /**
     * Socialise list: selects a nearby character to interact with. Input
     * {@code "0"} returns to the main menu.
     */
    private static boolean handleSocialise(String input, Location loc,
            GameState state, WorldRegistry world) {
        if (input.equals("0")) {
            setStep(Step.MAIN);
            return true;
        }
        List<models.Character> chars = charsAt(loc, state, world);
        return pickFromList(input, chars, idx -> {
            selectedCharacter = chars.get(idx);
            setStep(Step.SOCIALISE_ACTION);
        });
    }

    /**
     * Socialise action: applies the chosen {@link InteractionType} to the
     * selected character. Adjusts the Social need and adds a notification.
     * Input {@code "0"} goes back to the character list.
     */
    private static boolean handleSocialiseAction(String input, SimCharacter player,
            GameState state) {
        if (input.equals("0")) {
            selectedCharacter = null;
            setStep(Step.SOCIALISE);
            return true;
        }
        InteractionType[] types = InteractionType.values();
        return pickFromList(input, List.of(types), idx -> {
            InteractionType chosen = types[idx];
            String result = state.getRelationshipService().interact(player, selectedCharacter, chosen);
            player.adjustNeed("Social", chosen.getValue());
            player.addNotification(result);
            selectedCharacter = null;
            setStep(Step.MAIN);
        });
    }

    /**
     * Change location: moves the player to the chosen location. Input
     * {@code "0"} returns to the main menu.
     */
    private static boolean handleChangeLocation(String input, SimCharacter player,
            WorldRegistry world) {
        if (input.equals("0")) {
            setStep(Step.MAIN);
            return true;
        }
        List<Location> locs = new ArrayList<>(world.getAllLocations());
        return pickFromList(input, locs, idx -> {
            player.setLocation(locs.get(idx));
            setStep(Step.MAIN);
        });
    }

    /**
     * Switch character: changes the active player to the chosen sim. Input
     * {@code "0"} returns to the main menu.
     */
    private static boolean handleSwitchCharacter(String input, GameState state) {
        if (input.equals("0")) {
            setStep(Step.MAIN);
            return true;
        }
        List<SimCharacter> sims = state.getSims();
        return pickFromList(input, sims, idx -> {
            state.setActivePlayer(sims.get(idx));
            setStep(Step.MAIN);
        });
    }

    // ── Shared input helper ───────────────────────────────────────────────────
    /**
     * Parses {@code input} as a 1-based index into {@code list}, calls
     * {@code action} with the resolved 0-based index on success, and shows an
     * inline error on failure.
     *
     * @param input the raw player input
     * @param list the list being indexed (used for bounds checking)
     * @param action called with the valid 0-based index
     * @return {@code true} if the index was valid and {@code action} ran;
     * {@code false} if the input was invalid and an error was shown
     */
    private static boolean pickFromList(String input, List<?> list, IndexAction action) {
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= list.size()) {
                throw new NumberFormatException();
            }
            action.run(idx);
            return true;
        } catch (NumberFormatException e) {
            Renderer.showError("Enter a number from the list, or 0 to go back.");
            return false;
        }
    }

    /**
     * Functional interface for the index-based callback in
     * {@link #pickFromList}.
     */
    @FunctionalInterface
    private interface IndexAction {

        void run(int idx);
    }

    // ── Accessors for Renderer ────────────────────────────────────────────────
    /**
     * Returns the current menu step.
     */
    public static Step getStep() {
        return step;
    }

    /**
     * Returns the furniture selected in the Interactables step, or
     * {@code null}.
     */
    public static Furniture getSelectedFurniture() {
        return selectedFurniture;
    }

    /**
     * Returns the character selected in the Socialise step, or {@code null}.
     */
    public static models.Character getSelectedCharacter() {
        return selectedCharacter;
    }

    /**
     * Returns all characters present at {@code loc}: other player sims first,
     * then NPCs. Excludes the active player. Used by both this controller and
     * the {@link ui.Renderer}.
     *
     * @param loc the location to query
     * @param state the live game state
     * @param world the world registry
     * @return characters at the given location, excluding the active player
     */
    public static List<models.Character> charsAt(Location loc, GameState state,
            WorldRegistry world) {
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

    private static void setStep(Step next) {
        step = next;
    }
}
