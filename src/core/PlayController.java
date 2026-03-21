package core;

import Types.AchievementList;
import Types.CareerList;
import Types.InteractionList;
import Types.ShopInventory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import models.House;
import models.Location;
import models.SimCharacter;
import models.actions.Furniture;
import models.debuffs.DebuffRegistry;
import services.FurnitureService;
import services.HouseService;
import services.NeedService;
import services.NotificationService;
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
        PICK_CAREER, // Career selection — triggered by interacting with the Work Desk
        SHOP, SHOP_HOUSES, SHOP_FURNITURE, 
        /**
         * Furniture selling step: allows player to select furniture from their house to sell.
         * Routes to {@link #handleSellFurniture}.
         */
        SELL_FURNITURE, // Shop sub-menus
    }

    // ── Session state ─────────────────────────────────────────────────────────
    private static Step step = Step.MAIN;
    private static Furniture selectedFurniture = null;
    private static models.Character selectedCharacter = null;
    private static List<House> currentHouses = null;
    private static List<Furniture> currentFurniture = null;

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
        NotificationService.tick(state.getActivePlayer());

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
                handleSocialiseAction(input, player, state, world);
            case CHANGE_LOCATION ->
                handleChangeLocation(input, player, world);
            case SWITCH_CHARACTER ->
                handleSwitchCharacter(input, state);
            case PICK_CAREER ->
                handlePickCareer(input, player, state);
            case SHOP ->
                handleShop(input, player, state);
            case SHOP_HOUSES ->
                handleShopHouses(input, player, state);
            case SHOP_FURNITURE ->
                handleShopFurniture(input, player, state);
            case SELL_FURNITURE ->
                handleSellFurniture(input, player, state);
        };
    }

    // ── Sub-handlers ──────────────────────────────────────────────────────────
    /**
     * Main menu: handles top-level player actions.
     *
     * <p>
     * Routes input to the following sub-menus or actions:
     * <ul>
     * <li>Option 1 → {@link Step#INTERACTABLES}: interact with location objects.</li>
     * <li>Option 2 → {@link Step#SOCIALISE}: socialize with nearby characters.</li>
     * <li>Option 3 → {@link Step#CHANGE_LOCATION}: move to a different location.</li>
     * <li>Option 4 → {@link Step#SWITCH_CHARACTER}: switch active player Sim.</li>
     * <li>Option 5 → {@link Step#SHOP}: access the shop menu.</li>
     * <li>Option 6 → Exits the game via {@link GameState.Phase#QUIT}.</li>
     * </ul>
     *
     * @param input the player's menu selection ("1"-"6")
     * @param state the {@link GameState}
     * @return {@code true} if the step changed; {@code false} if input was invalid
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
                setStep(Step.SHOP);
            case "6" ->
                state.setPhase(GameState.Phase.QUIT);
            default -> {
                Renderer.showError("Invalid choice. Enter 1-5.");
                return false;
            }
        }
        return true;
    }

    /**
     * Career selection menu: shown when a jobless Sim interacts with the Work Desk.
     *
     * <p>
     * Displays all available careers from {@link CareerList} (excluding JOBLESS),
     * with salary, working hours, and related skills. Selecting a career immediately
     * joins it via {@link SimCharacter#joinCareer(CareerList)} and triggers achievement
     * evaluation. Input {@code "0"} cancels back to {@link Step#MAIN}.
     *
     * @param input the player's career selection (career number or "0")
     * @param player the active {@link SimCharacter}}
     * @param state the {@link GameState}
     * @return {@code true} if the step changed; {@code false} if input was invalid
     */
    private static boolean handlePickCareer(String input, SimCharacter player, GameState state) {
        if (input.equals("0")) {
            setStep(Step.MAIN);
            return true;
        }
        return pickFromList(input, AVAILABLE_CAREERS, idx -> {
            CareerList chosen = AVAILABLE_CAREERS.get(idx);
            player.joinCareer(chosen);
            addAchievementNotifications(
                    player,
                    state.getAchievementService().evaluateCareerAchievements(player));
            NotificationService.add(player, "Career started: " + chosen.getTitle()
                    + ". Head to the Office to work!");
            setStep(Step.MAIN);
        });
    }

    /**
     * Shop menu: handles house and furniture transactions.
     *
     * <p>
     * Routes player input to the appropriate sub-menu:
     * <ul>
     * <li>Option 1 → {@link Step#SHOP_HOUSES}: browse available houses for purchase.</li>
     * <li>Option 2 → {@link Step#SHOP_FURNITURE}: browse available furniture for purchase.</li>
     * <li>Option 3 → {@link Step#SELL_FURNITURE}: sell furniture from the player's current house.</li>
     * <li>Option 0 → {@link Step#MAIN}: return to main menu.</li>
     * </ul>
     *
     * @param input the player's menu selection
     * @param player the active {@link SimCharacter}
     * @param state the {@link GameState}
     * @return {@code true} if the step changed; {@code false} if validation failed
     */
    private static boolean handleShop(String input, SimCharacter player, GameState state) {
        if (input.equals("0")) {
            setStep(Step.MAIN);
            return true;
        }

        switch (input) {
            case "1" -> {
                // Houses
                currentHouses = ShopInventory.getAvailableHouses();
                currentHouses.removeIf(House::isOwned); // Filter out owned houses
                if (currentHouses.isEmpty()) {
                    NotificationService.add(player, "No houses available for purchase.");
                    return false;
                }
                setStep(Step.SHOP_HOUSES);
                return true;
            }
            case "2" -> {
                if (player.getCurrentHouse() == null) {
                    NotificationService.add(player, "You must own a house to purchase furniture! Buy a house first.");
                    return false;
                }
                // Furniture
                currentFurniture = ShopInventory.getAvailableFurniture();
                setStep(Step.SHOP_FURNITURE);
                return true;
            }
            case "3" -> {
                // Sell furniture from current house
                if (player.getCurrentHouse() == null) {
                    NotificationService.add(player, "You must own a house to sell furniture!");
                    return false;
                }
                currentFurniture = new ArrayList<>(player.getCurrentHouse().getFurnitures());
                if (currentFurniture.isEmpty()) {
                    NotificationService.add(player, "Your house has no furniture to sell.");
                    return false;
                }
                setStep(Step.SELL_FURNITURE);
                return true;
            }
            default -> {
                Renderer.showError("Enter 1, 2, 3, or 0 to go back.");
                return false;
            }
        }
    }

    private static boolean handleShopHouses(String input, SimCharacter player, GameState state) {
        if (input.equals("0")) {
            currentHouses = null;
            setStep(Step.SHOP);
            return true;
        }

        return pickFromList(input, currentHouses, idx -> {
            House house = currentHouses.get(idx);
            boolean success = HouseService.purchaseHouse(player, house);

            if (success) {
                player.setCurrentHouse(house);
                NotificationService.add(player, HouseService.getPurchaseMessage(player, house, true));
            } else {
                NotificationService.add(player, HouseService.getPurchaseMessage(player, house, false));
            }
            setStep(Step.SHOP);
            currentHouses = null;

        });
    }

    private static boolean handleShopFurniture(String input, SimCharacter player, GameState state) {
        if (input.equals("0")) {
            currentFurniture = null;
            setStep(Step.SHOP);
            return true;
        }

        return pickFromList(input, currentFurniture, idx -> {
            Furniture furniture = currentFurniture.get(idx);
            House house = player.getCurrentHouse();

            boolean success = FurnitureService.purchaseFurniture(player, house, furniture);

            if (success) {
                NotificationService.add(player, FurnitureService.getPurchaseMessage(player, house, furniture, true));
            } else {
                NotificationService.add(player, FurnitureService.getPurchaseMessage(player, house, furniture, false));
            }
            setStep(Step.SHOP);
            currentFurniture = null;
        });
    }

    /**
     * Furniture seller: handles selling furniture from the player's house.
     *
     * <p>
     * Lists all furniture in the player's current house and allows selection by number.
     * When a furniture item is selected, {@link FurnitureService#sellFurniture} is called
     * to process the sale, refunding 50% of the original purchase price. The furniture
     * is removed from the house inventory. Input {@code "0"} returns to the shop menu.
     *
     * @param input the player's selection (furniture number or "0")
     * @param player the active {@link SimCharacter}
     * @param state the {@link GameState}
     * @return {@code true} if the step changed; {@code false} if selection was invalid
     */
    private static boolean handleSellFurniture(String input, SimCharacter player, GameState state) {
        if (input.equals("0")) {
            currentFurniture = null;
            setStep(Step.SHOP);
            return true;
        }

        return pickFromList(input, currentFurniture, idx -> {
            Furniture furniture = currentFurniture.get(idx);
            House house = player.getCurrentHouse();

            boolean success = FurnitureService.sellFurniture(player, house, furniture);

            if (success) {
                NotificationService.add(player, FurnitureService.getSellMessage(player, house, furniture, true));
            } else {
                NotificationService.add(player, FurnitureService.getSellMessage(player, house, furniture, false));
            }
            setStep(Step.SHOP);
            currentFurniture = null;
        });
    }

    /**
     * Interactables list: displays all furniture at the player's current location.
     *
     * <p>
     * Lists available {@link Furniture} items that the Sim can interact with.
     * Selecting an item by number routes to {@link Step#INTERACTABLE_ACTION} where
     * the player can choose a specific action (e.g., Sleep, Eat, Use). Input {@code "0"}
     * returns to {@link Step#MAIN}.
     *
     * @param input the player's furniture selection (furniture number or "0")
     * @param loc the {@link Location} where the Sim currently is
     * @return {@code true} if the step changed; {@code false} if input was invalid
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
        actions.sort(String::compareTo);
        return pickFromList(input, actions, idx -> {
            String actionName = actions.get(idx);

            // Intercept the Work Desk action
            if ("Work Desk".equals(selectedFurniture.getName()) && "Work".equals(actionName)) {
                if (player.getCareer().getCurrentCareer() == CareerList.JOBLESS) {
                    // No job yet — route to career picker
                    setStep(Step.PICK_CAREER);
                } else {
                    // Has a job — run the shift
                    String result = WorkService.work(
                            player,
                            state.getGameClock());
                    addAchievementNotifications(
                            player,
                            state.getAchievementService().evaluateWorkAchievements(player));
                    NotificationService.add(player, result);
                    setStep(Step.MAIN);
                }
            } else {
                // Pass the clock so timeRequired advances in-game time
                models.actions.FurnitureAction action = selectedFurniture.getAction(actionName);
                boolean ok = (action != null)
                        && action.perform(player, state.getGameClock());
                if (!ok) {
                    NotificationService.add(player, "Action failed: not enough money or needs too low.");
                } else if (action != null) {
                    addSkillAchievementNotifications(
                            player,
                            action,
                            state);
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
            GameState state, WorldRegistry world) {
        if (input.equals("0")) {
            selectedCharacter = null;
            setStep(Step.SOCIALISE);
            return true;
        }
        InteractionList[] types = InteractionList.values();
        return pickFromList(input, List.of(types), idx -> {
            InteractionList chosen = types[idx];

            String blockReason = DebuffRegistry.getInteractionBlockReason(player, "Socialise");
            if (blockReason != null) {
                NotificationService.add(player, selectedCharacter.getName() + " refused to interact! " + blockReason);
                selectedCharacter = null;
                setStep(Step.MAIN);
                return;
            }

            String result = state.getRelationshipService().interact(player, selectedCharacter, chosen);
            NeedService.adjustNeed(player, "Social", chosen.getEffect());
            addAchievementNotifications(
                    player,
                    state.getAchievementService().evaluateSocialAchievements(
                            player,
                            getAllCharacters(state, world),
                            state.getRelationshipService()));
            NotificationService.add(player, result);
            selectedCharacter = null;
            setStep(Step.MAIN);
        });
    }

    /**
     * Location selection menu: move the Sim to a different location.
     *
     * <p>
     * Displays all available world locations. Selecting a location by number updates
     * the Sim's location via {@link SimCharacter#setLocation(Location)} and returns to
     * {@link Step#MAIN}. The current location is highlighted in the menu. Input {@code "0"}
     * returns to {@link Step#MAIN}.
     *
     * @param input the player's location selection (location number or "0")
     * @param player the active {@link SimCharacter}}
     * @param world the {@link WorldRegistry} providing the list of all locations
     * @return {@code true} if the step changed; {@code false} if input was invalid
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
     * Sends achievement unlock notifications to the player.
     *
     * <p>
     * For each newly unlocked {@link AchievementList}, adds a formatted notification
     * message via {@link NotificationService#add(SimCharacter, String)}.
     *
     * @param player the {@link SimCharacter} who unlocked the achievements
     * @param unlockedAchievements a list of {@link AchievementList} values that were just unlocked
     */
    private static void addAchievementNotifications(
            SimCharacter player,
            List<AchievementList> unlockedAchievements) {
        for (AchievementList achievement : unlockedAchievements) {
            NotificationService.add(player, "Achievement unlocked: " + achievement.getTitle());
        }
    }

    /**
     * Sends achievement notifications for skill milestones triggered by an action.
     *
     * <p>
     * For each skill affected by the {@link models.actions.FurnitureAction},
     * evaluates whether a first-time skill achievement was unlocked and sends
     * notifications via {@link #addAchievementNotifications}.
     *
     * @param player the {@link SimCharacter} who performed the action
     * @param action the {@link models.actions.FurnitureAction} that was performed
     * @param state the {@link GameState} for achievement evaluation
     */
    private static void addSkillAchievementNotifications(
            SimCharacter player,
            models.actions.FurnitureAction action,
            GameState state) {
        for (String skill : action.affectedSkillsByActionMap().keySet()) {
            addAchievementNotifications(
                    player,
                    state.getAchievementService().evaluateFirstTimeSkillAchievement(player, skill));
        }
    }

    /**
     * Returns a combined list of all player Sims and NPCs in the world.
     *
     * @param state the {@link GameState} providing player Sims
     * @param world the {@link WorldRegistry} providing all NPCs
     * @return a {@link List} containing all {@link SimCharacter}s and {@link models.NPCCharacter}s
     */
    private static List<models.Character> getAllCharacters(GameState state, WorldRegistry world) {
        List<models.Character> characters = new ArrayList<>();
        characters.addAll(state.getSims());
        characters.addAll(world.getAllNPCs());
        return characters;
    }

    /**
     * Character switcher: change the active player Sim.
     *
     * <p>
     * Displays all player Sims with the currently active one highlighted.
     * Selecting a Sim by number updates the active player via {@link GameState#setActivePlayer(SimCharacter)}
     * and returns to {@link Step#MAIN}. Input {@code "0"} cancels back to {@link Step#MAIN}.
     *
     * @param input the player's Sim selection (Sim number or "0")
     * @param state the {@link GameState}
     * @return {@code true} if the step changed; {@code false} if input was invalid
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
     * Generic list selection helper: parses user input and executes the specified action.
     *
     * <p>
     * Interprets {@code input} as a 1-based list index, validates it falls within the
     * list bounds, and executes the {@link IndexAction} callback on the selected index.
     * If parsing fails or the index is out of bounds, displays an inline error message
     * via {@link Renderer#showError}.
     *
     * @param input the player's selection (1-based list index)
     * @param list the {@link List} of selectable items
     * @param action the {@link IndexAction} callback to execute with the selected index
     * @return {@code true} if selection was successful; {@code false} if input was invalid
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
     * Functional interface for handling list index selection actions.
     *
     * <p>
     * Implementations define what happens when a user selects an item from a list.
     * Used by {@link #pickFromList} to decouple list handling from action logic.
     */
    @FunctionalInterface
    private interface IndexAction {
        /**
         * Execute the action for the selected list item.
         *
         * @param idx the 0-based index of the selected item
         */
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
     * Returns the list of houses in the current world.
     */
    public static List<House> getCurrentHouses() {
        return currentHouses;
    }

    /**
     * Returns the list of furniture available for purchase in the current world.
     */
    public static List<Furniture> getCurrentFurniture() {
        return currentFurniture;
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

    /**
     * Updates the current menu step and triggers a UI redraw on next render.
     *
     * @param next the {@link Step} to transition to
     */
    private static void setStep(Step next) {
        step = next;
    }
}
