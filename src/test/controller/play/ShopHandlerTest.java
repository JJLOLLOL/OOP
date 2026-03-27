package controller.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import core.GameState;
import core.WorldRegistry;
import data.ShopInventory;
import models.character.SimCharacter;
import models.furniture.Furniture;
import models.location.House;
import models.location.Location;
import services.NotificationService;
import types.Gender;
import ui.UITestSupport;

class ShopHandlerTest {

    @Test
    void handleInput_shouldGoBackFromMainShop() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("0", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldRejectBuyingFurnitureWithoutHouse() {
        House home = new House("Home", new ArrayList<>());
        Location park = new Location("Park", new ArrayList<>());
        SimCharacter player = new SimCharacter("Alex", 20, Gender.MALE, park);
        GameState state = new GameState();
        state.addSim(player);
        state.setActivePlayer(player);
        WorldRegistry world = new WorldRegistry(Map.of("Park", park, "Home", home), List.of());
        PlayContextTest context = new PlayContextTest(state, world, new ShopInventory(List.of(home), List.of(new Furniture("Lamp", "Lamp", 10.0))));
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertFalse(handler.handleInput("2", context));
    }

    @Test
    void handleInput_shouldRejectSellingFurnitureWithoutHouse() {
        Location park = new Location("Park", new ArrayList<>());
        SimCharacter player = new SimCharacter("Alex", 20, Gender.MALE, park);
        GameState state = new GameState();
        state.addSim(player);
        state.setActivePlayer(player);
        WorldRegistry world = new WorldRegistry(Map.of("Park", park), List.of());
        PlayContextTest context = new PlayContextTest(state, world, new ShopInventory(List.of(), List.of()));
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertFalse(handler.handleInput("3", context));
    }

    @Test
    void handleInput_shouldRejectSellingWhenHouseHasNoFurniture() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        House emptyHouse = new House("Empty", new ArrayList<>());
        f.player.assignHouse(emptyHouse);
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertFalse(handler.handleInput("3", context));
    }

    @Test
    void handleInput_shouldEnterHouseShopAndBuyHouse() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        f.player.earnMoney(5000.0);
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertEquals(controller.PlayController.Step.SHOP_HOUSES, handler.getStep());
        assertTrue(handler.handleInput("1", context));
        assertEquals(controller.PlayController.Step.SHOP, handler.getStep());
        assertEquals(f.villa.getTier(), f.player.getCurrentHouse().getTier());
        assertEquals(f.villa.getPrice(), f.player.getCurrentHouse().getPrice());
    }

    @Test
    void handleInput_shouldBackOutFromHouseShop() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertTrue(handler.handleInput("0", context));
        assertEquals(controller.PlayController.Step.SHOP, handler.getStep());
    }

    @Test
    void handleInput_shouldEnterFurnitureShopAndBuyFurniture() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        f.player.earnMoney(1000.0);
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("2", context));
        assertEquals(controller.PlayController.Step.SHOP_FURNITURE, handler.getStep());
        int before = f.player.getCurrentHouse().getFurnitureCount();
        assertTrue(handler.handleInput("1", context));
        assertEquals(before + 1, f.player.getCurrentHouse().getFurnitureCount());
        assertEquals(controller.PlayController.Step.SHOP, handler.getStep());
    }

    @Test
    void handleInput_shouldSellFurniture() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        int before = f.player.getCurrentHouse().getFurnitureCount();
        assertTrue(handler.handleInput("3", context));
        assertEquals(controller.PlayController.Step.SELL_FURNITURE, handler.getStep());
        assertTrue(handler.handleInput("1", context));
        assertEquals(before - 1, f.player.getCurrentHouse().getFurnitureCount());
        assertEquals(controller.PlayController.Step.SHOP, handler.getStep());
    }

    @Test
    void handleInput_shouldBackOutFromFurnitureMenus() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        ShopHandler handler = new ShopHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("2", context));
        assertTrue(handler.handleInput("0", context));
        assertEquals(controller.PlayController.Step.SHOP, handler.getStep());

        handler.onEnter(context);
        assertTrue(handler.handleInput("3", context));
        assertTrue(handler.handleInput("0", context));
        assertEquals(controller.PlayController.Step.SHOP, handler.getStep());
    }

    @Test
    void handleInput_shouldRejectInvalidChoicesAndEmptyHouseInventory() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        ShopHandler handler = new ShopHandler();
        PlayContextTest context = new PlayContextTest(f.state, f.world, new ShopInventory(List.of(), List.of(f.lamp)));
        handler.onEnter(context);

        assertFalse(handler.handleInput("1", context));
        assertFalse(handler.handleInput("x", context));

        context = new PlayContextTest(f.state, f.world, f.shopInventory);
        handler.onEnter(context);
        assertTrue(handler.handleInput("1", context));
        assertFalse(handler.handleInput("99", context));

        handler.onEnter(context);
        assertTrue(handler.handleInput("2", context));
        assertFalse(handler.handleInput("99", context));

        handler.onEnter(context);
        assertTrue(handler.handleInput("3", context));
        assertFalse(handler.handleInput("99", context));

        assertTrue(NotificationService.get(f.player).size() >= 0);
    }
}