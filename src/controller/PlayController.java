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
import types.AchievementList;
import ui.Renderer;

public class PlayController implements PlayContext {

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

    public boolean handleInput(String input, GameState state, WorldRegistry world) {
        this.gameState = state;
        this.worldRegistry = world;
        return activeHandler.handleInput(input, this);
    }

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

    @Override
    public void switchTo(HandlerType type) {
        this.activeHandler = handlers.get(type);
        if (this.activeHandler == null) {
            throw new IllegalStateException("No handler registered for type: " + type);
        }
        // Call onEnter to reset the handler's state
        this.activeHandler.onEnter(this);
    }

    public static void addAchievementNotifications(
            SimCharacter player,
            List<AchievementList> unlockedAchievements) {
        for (AchievementList achievement : unlockedAchievements) {
            NotificationService.add(player, "Achievement unlocked: " + achievement.getTitle());
        }
    }

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

    public static List<models.character.Character> getAllCharacters(GameState state, WorldRegistry world) {
        List<models.character.Character> characters = new ArrayList<>();
        characters.addAll(state.getSims());
        characters.addAll(world.getAllNPCs());
        return characters;
    }

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

    @FunctionalInterface
    public interface IndexAction {
        void run(int idx);
    }

    public PlayInputHandler getActiveHandler() {
        return activeHandler;
    }

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
