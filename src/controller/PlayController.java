package controller;

import controller.play.*;
import data.ShopInventory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import core.GameState;
import core.WorldRegistry;
import models.character.SimCharacter;
import models.location.Location;
import models.skill.SkillType;
import services.NotificationService;
import types.AchievementType;
import ui.Renderer;

/**
 * Main controller for the gameplay phase. It owns the handler registry, keeps
 * the active gameplay context current, and routes input to the correct
 * sub-handler.
 */
public class PlayController implements PlayContext {

    /**
     * UI steps that determine which gameplay menu or sub-menu is rendered.
     */
    public enum Step {
        MAIN,
        INTERACTABLES, INTERACTABLE_ACTION,
        SOCIALISE, SOCIALISE_ACTION,
        CHANGE_LOCATION,
        SWITCH_CHARACTER,
        PICK_CAREER, // Career selection — triggered by interacting with the Work Desk
        SHOP, SHOP_HOUSES, SHOP_FURNITURE,
        SELL_FURNITURE, // Shop sub-menus
    }

    private final Map<HandlerType, PlayInputHandler> handlers = new EnumMap<>(HandlerType.class);
    private PlayInputHandler activeHandler;
    private GameState gameState;
    private WorldRegistry worldRegistry;
    private final ShopInventory shopInventory;

    /**
     * Routes raw input to the currently active gameplay handler.
     *
     * @param input the player's raw input
     * @param state the shared game state
     * @param world the loaded world registry
     * @return {@code true} when the UI should redraw, {@code false} when the
     * active handler displayed an inline error
     */
    public boolean handleInput(String input, GameState state, WorldRegistry world) {
        this.gameState = state;
        this.worldRegistry = world;
        return activeHandler.handleInput(input, this);
    }

    /**
     * Creates the controller and registers each gameplay handler.
     *
     * @param shopInventory the immutable inventory exposed by the shop menus
     */
    public PlayController(ShopInventory shopInventory) {
        this.shopInventory = shopInventory;

        handlers.put(HandlerType.MAIN_MENU, new MainMenuHandler());
        handlers.put(HandlerType.INTERACTION, new InteractionHandler());
        handlers.put(HandlerType.SOCIAL, new SocialHandler());
        handlers.put(HandlerType.LOCATION_CHANGE, new LocationChangeHandler());
        handlers.put(HandlerType.SWITCH_CHARACTER, new SwitchCharacterHandler());
        handlers.put(HandlerType.PICK_CAREER, new PickCareerHandler());
        handlers.put(HandlerType.SHOP, new ShopHandler());

        // Set initial handler
        this.activeHandler = handlers.get(HandlerType.MAIN_MENU);
    }

    @Override
    public GameState getGameState() { return gameState; }

    @Override
    public WorldRegistry getWorldRegistry() { return worldRegistry; }

    @Override
    public ShopInventory getShopInventory() { return shopInventory; }

    @Override
    public SimCharacter getActivePlayer() { return gameState.getActivePlayer(); }

    /**
     * Switches the active gameplay handler and runs that handler's setup hook
     * before the next render.
     *
     * @param type the handler type to activate
     */
    @Override
    public void switchTo(HandlerType type) {
        this.activeHandler = handlers.get(type);
        if (this.activeHandler == null) {
            throw new IllegalStateException("No handler registered for type: " + type);
        }
        // Call onEnter to reset the handler's state
        this.activeHandler.onEnter(this);
    }

    /**
     * Converts unlocked achievements into player-facing notifications.
     *
     * @param player the sim who unlocked the achievements
     * @param unlockedAchievements the achievements to announce
     */
    public static void addAchievementNotifications(
            SimCharacter player,
            List<AchievementType> unlockedAchievements) {
        for (AchievementType achievement : unlockedAchievements) {
            NotificationService.add(player, "Achievement unlocked: " + achievement.getTitle());
        }
    }

    /**
     * Evaluates first-use skill achievements for every skill touched by a
     * furniture action and emits notifications for any newly unlocked ones.
     *
     * @param player the acting sim
     * @param action the furniture action that granted skill progress
     * @param state the current game state used to access the achievement service
     */
    public static void addSkillAchievementNotifications(
            SimCharacter player,
            models.furniture.FurnitureAction action,
            GameState state) {
        for (SkillType skill : action.affectedSkillsByActionMap().keySet()) {
            addAchievementNotifications(
                    player,
                    state.getAchievementService().evaluateFirstTimeSkillAchievement(player, skill));
        }
    }

    /**
     * Returns a combined list of every sim and NPC in the current world.
     *
     * @param state the current game state
     * @param world the current world registry
     * @return all controllable and non-playable characters
     */
    public static List<models.character.Character> getAllCharacters(GameState state, WorldRegistry world) {
        List<models.character.Character> characters = new ArrayList<>();
        characters.addAll(state.getSims());
        characters.addAll(world.getAllNPCs());
        return characters;
    }

    /**
     * Parses a one-based menu selection and invokes a callback for the chosen
     * index.
     *
     * @param input the raw menu choice entered by the player
     * @param list the list being selected from
     * @param action the callback to run with the resolved zero-based index
     * @return {@code true} when a valid selection was made
     */
    public static boolean pickFromList(String input, List<?> list, IndexAction action) {
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
     * Callback used by {@link #pickFromList(String, List, IndexAction)} once a
     * valid menu index has been resolved.
     */
    @FunctionalInterface
    public interface IndexAction {
        /**
         * Performs work for the selected zero-based index.
         *
         * @param idx the validated zero-based index
         */
        void run(int idx);
    }

    public PlayInputHandler getActiveHandler() {
        return activeHandler;
    }

    /**
     * Returns all non-active characters currently standing in the supplied
     * location.
     *
     * @param loc the location to inspect
     * @param state the current game state
     * @param world the world registry providing NPCs
     * @return nearby sims and NPCs, excluding the active player
     */
    public static List<models.character.Character> charsAt(Location loc, GameState state,
            WorldRegistry world) {
        SimCharacter player = state.getActivePlayer();
        List<models.character.Character> chars = new ArrayList<>();
        state.getSims().stream()
                .filter(s -> !s.equals(player) && s.getLocation().equals(loc))
                .forEach(chars::add);
        world.getAllNPCs().stream()
                .filter(n -> n.getLocation().equals(loc))
                .forEach(chars::add);
        return chars;
    }
}
