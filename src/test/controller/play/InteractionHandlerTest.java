// src/test/controller/play/InteractionHandlerTest.java
package controller.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;

import core.GameState;
import data.ShopInventory;
import models.career.CareerList;
import models.character.SimCharacter;
import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.location.Location;
import models.need.NeedType;
import services.NotificationService;
import types.Gender;
import ui.UITestSupport;

class InteractionHandlerTest {

    @Test
    void handleInput_shouldGoBackFromInteractables() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        InteractionHandler handler = new InteractionHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("0", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldEnterAndBackOutOfActionMenu() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        InteractionHandler handler = new InteractionHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertEquals(controller.PlayController.Step.INTERACTABLE_ACTION, handler.getStep());
        assertTrue(handler.getSelectedFurniture() != null);

        assertTrue(handler.handleInput("0", context));
        assertEquals(controller.PlayController.Step.INTERACTABLES, handler.getStep());
        assertNull(handler.getSelectedFurniture());
        assertNull(handler.getSortedActionNames());
    }

    @Test
    void handleInput_shouldRouteJoblessWorkDeskToPickCareer() {
        Furniture workDesk = new Furniture("Work Desk", "Desk", 100.0);
        workDesk.addAction(new FurnitureAction("Work", "Do work", Map.of(), Map.of(), 0.0, 0.0));
        Location office = new Location("Office", new ArrayList<>(java.util.List.of(workDesk)));

        SimCharacter player = new SimCharacter("Alex", 20, Gender.MALE, office);
        GameState state = new GameState();
        state.addSim(player);
        state.setActivePlayer(player);

        core.WorldRegistry world = new core.WorldRegistry(Map.of("Office", office), java.util.List.of());
        PlayContextTest context = new PlayContextTest(state, world, new ShopInventory(java.util.List.of(), java.util.List.of()));
        InteractionHandler handler = new InteractionHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertTrue(handler.handleInput("1", context));
        assertEquals(HandlerType.PICK_CAREER, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldPerformWorkForEmployedSim() {
        Furniture workDesk = new Furniture("Work Desk", "Desk", 100.0);
        workDesk.addAction(new FurnitureAction("Work", "Do work", Map.of(), Map.of(), 0.0, 0.0));
        Location office = new Location("Office", new ArrayList<>(java.util.List.of(workDesk)));

        SimCharacter player = new SimCharacter("Alex", 20, Gender.MALE, office);
        player.joinCareer(CareerList.SOFTWARE_DEVELOPER);
        SimCharacter.setWorkAction(new FurnitureAction("Work", "Work", Map.of(NeedType.ENERGY, -1.0), Map.of(), 0.0, 0.0));

        GameState state = new GameState();
        state.getGameClock().advanceHours(1.0);
        state.addSim(player);
        state.setActivePlayer(player);

        core.WorldRegistry world = new core.WorldRegistry(Map.of("Office", office), java.util.List.of());
        PlayContextTest context = new PlayContextTest(state, world, new ShopInventory(java.util.List.of(), java.util.List.of()));
        InteractionHandler handler = new InteractionHandler();
        handler.onEnter(context);

        double moneyBefore = player.getMoney();
        assertTrue(handler.handleInput("1", context));
        assertTrue(handler.handleInput("1", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
        assertTrue(player.getMoney() > moneyBefore);
    }

    @Test
    void handleInput_shouldPerformRegularActionAndReturnToMainMenu() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        InteractionHandler handler = new InteractionHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertTrue(handler.handleInput("1", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldNotifyWhenRegularActionFails() {
        Furniture expensive = new Furniture("Console", "Game console", 500.0);
        expensive.addAction(new FurnitureAction("Play", "Too expensive", Map.of(), Map.of(), 9999.0, 0.0));
        Location room = new Location("Room", new ArrayList<>(java.util.List.of(expensive)));

        SimCharacter player = new SimCharacter("Alex", 20, Gender.MALE, room);
        GameState state = new GameState();
        state.addSim(player);
        state.setActivePlayer(player);

        core.WorldRegistry world = new core.WorldRegistry(Map.of("Room", room), java.util.List.of());
        PlayContextTest context = new PlayContextTest(state, world, new ShopInventory(java.util.List.of(), java.util.List.of()));
        InteractionHandler handler = new InteractionHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertTrue(handler.handleInput("1", context));
        assertTrue(NotificationService.get(player).stream()
                .anyMatch(m -> m.contains("Action failed: not enough money or needs too low.")));
    }

    @Test
    void handleInput_shouldRejectInvalidSelection() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        InteractionHandler handler = new InteractionHandler();
        handler.onEnter(context);

        assertFalse(handler.handleInput("99", context));
    }
}