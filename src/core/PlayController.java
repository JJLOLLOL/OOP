package core;

import Types.CareerList;
import Types.InteractionType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import models.Location;
import models.SimCharacter;
import models.actions.Furniture;
import services.WorkService;
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
 * {@link #handleInput} returns {@code true} when the step changed so
 * {@link GameEngine} knows to trigger a redraw, and {@code false} when an
 * inline error was shown instead (so the error stays visible).
 */
public class PlayController {

    // ── Step enum ─────────────────────────────────────────────────────────────
    /**
     * The active sub-menu within the playing phase. {@link ui.Renderer}
     * switches on this to display the correct panel.
     */
    public enum Step {
        MAIN,
        INTERACTABLES, INTERACTABLE_ACTION,
        SOCIALISE, SOCIALISE_ACTION,
        CHANGE_LOCATION,
        SWITCH_CHARACTER,
        PICK_CAREER    // Career selection — triggered by interacting with the Work Desk
    }

    // ── Session state ─────────────────────────────────────────────────────────
    private static Step step = Step.MAIN;
    private static Furniture selectedFurniture = null;
    private static models.Character selectedCharacter = null;

    /**
     * Available careers shown in the PICK_CAREER screen (excludes JOBLESS).
     */
    private static final List<CareerList> AVAILABLE_CAREERS = Arrays.stream(CareerList.values())
            .filter(c -> c != CareerList.JOBLESS)
            .collect(Collectors.toList());

    // ── Entry point ───────────────────────────────────────────────────────────
    /**
     * Processes one line of player input for the current menu step.
     *
     * @param input the trimmed player input line
     * @param state the live game state
     * @param world the world registry
     * @return {@code true} if the step changed and the screen should redraw;
     * {@code false} if an inline error was shown
     */
    public static boolean handleInput(String input, GameState state, WorldRegistry world) {
        // Advance notification timer on every player action
        state.getActivePlayer().tickNotifications();

        SimCharacter player = state.getActivePlayer();
        Location loc = player.getLocation();

        return switch (step) {
            case MAIN ->
                handleMain(input, state);
            case INTERACTABLES ->
                handleInteractables(input, loc);
            case INTERACTABLE_ACTION ->
                handleInteractableAction(input, player, state);
            case SOCIALISE ->
                handleSocialise(input, loc, state, world);
            case SOCIALISE_ACTION ->
                handleSocialiseAction(input, player, state);
            case CHANGE_LOCATION ->
                handleChangeLocation(input, player, world);
            case SWITCH_CHARACTER ->
                handleSwitchCharacter(input, state);
            case PICK_CAREER ->
                handlePickCareer(input, player);
        };
    }

    // ── Sub-handlers ──────────────────────────────────────────────────────────
    /**
     * Main menu: options 1–5 always shown; option 6 (Work) shown only at the
     * Office.
     */
    /**
     * Main menu: options 1–5. Work is accessed via the Work Desk in
     * Interactables.
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
     * Career picker: shown when the sim is jobless and tries to work. Lists all
     * careers from {@link CareerList} (excluding JOBLESS). Input {@code "0"}
     * cancels back to main.
     */
    /**
     * Career picker: triggered when a jobless sim interacts with the Work Desk.
     * Selecting a career immediately starts the shift via {@link WorkService}.
     * Input {@code "0"} cancels back to main.
     */
    private static boolean handlePickCareer(String input, SimCharacter player) {
        if (input.equals("0")) {
            setStep(Step.MAIN);
            return true;
        }
        return pickFromList(input, AVAILABLE_CAREERS, idx -> {
            CareerList chosen = AVAILABLE_CAREERS.get(idx);
            player.joinCareer(chosen);
            player.addNotification("Career started: " + chosen.getTitle()
                    + ". Head to the Office to work!");
            setStep(Step.MAIN);
        });
    }

    /**
     * Interactables list: select furniture by number. {@code "0"} → main.
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
     * Furniture action: perform chosen action. {@code "0"} → interactables.
     */
    /**
     * Furniture action: performs the chosen action on the selected furniture.
     *
     * <p>
     * Special case — the Work Desk "Work" action is intercepted here:
     * <ul>
     * <li>Jobless sim → routes to {@link Step#PICK_CAREER}</li>
     * <li>Employed sim → calls {@link WorkService#work} directly</li>
     * </ul>
     * Input {@code "0"} goes back to the furniture list.
     */
    private static boolean handleInteractableAction(String input, SimCharacter player,
            GameState state) {
        if (input.equals("0")) {
            selectedFurniture = null;
            setStep(Step.INTERACTABLES);
            return true;
        }
        List<String> actions = new ArrayList<>(selectedFurniture.getActionNames());
        return pickFromList(input, actions, idx -> {
            String actionName = actions.get(idx);

            // Intercept the Work Desk action
            if ("Work Desk".equals(selectedFurniture.getName()) && "Work".equals(actionName)) {
                if (player.getCareer().getCurrentCareer() == CareerList.JOBLESS) {
                    // No job yet — route to career picker
                    setStep(Step.PICK_CAREER);
                } else {
                    // Has a job — run the shift
                    String result = WorkService.work(player, state.getGameClock());
                    player.addNotification(result);
                    setStep(Step.MAIN);
                }
            } else {
                // Pass the clock so timeRequired advances in-game time
                models.actions.FurnitureAction action = selectedFurniture.getAction(actionName);
                boolean ok = (action != null)
                        && action.perform(player, state.getGameClock());
                if (!ok) {
                    player.addNotification("Action failed: not enough money or needs too low.");
                }
                setStep(Step.MAIN);
            }
            selectedFurniture = null;
        });
    }

    /**
     * Socialise list: select a nearby character. {@code "0"} → main.
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
     * Socialise action: apply chosen interaction. {@code "0"} → socialise.
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
     * Change location: move player to chosen location. {@code "0"} → main.
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
     * Switch character: change active player. {@code "0"} → main.
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
     * {@code action} on success, shows an inline error on failure.
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
     * Returns the furniture selected in INTERACTABLE_ACTION, or {@code null}.
     */
    public static Furniture getSelectedFurniture() {
        return selectedFurniture;
    }

    /**
     * Returns the character selected in SOCIALISE_ACTION, or {@code null}.
     */
    public static models.Character getSelectedCharacter() {
        return selectedCharacter;
    }

    /**
     * Returns the list of selectable careers (excludes JOBLESS).
     */
    public static List<CareerList> getAvailableCareers() {
        return AVAILABLE_CAREERS;
    }

    /**
     * Returns all characters present at {@code loc}: other player sims first,
     * then NPCs. Excludes the active player.
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
