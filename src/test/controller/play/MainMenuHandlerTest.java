package controller.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import core.GameState;
import ui.UITestSupport;

class MainMenuHandlerTest {

    @Test
    void handleInput_shouldRouteAllValidOptions() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        MainMenuHandler handler = new MainMenuHandler();

        assertTrue(handler.handleInput("1", context));
        assertEquals(HandlerType.INTERACTION, context.getLastSwitchedTo());

        assertTrue(handler.handleInput("2", context));
        assertEquals(HandlerType.SOCIAL, context.getLastSwitchedTo());

        assertTrue(handler.handleInput("3", context));
        assertEquals(HandlerType.LOCATION_CHANGE, context.getLastSwitchedTo());

        assertTrue(handler.handleInput("4", context));
        assertEquals(HandlerType.SWITCH_CHARACTER, context.getLastSwitchedTo());

        assertTrue(handler.handleInput("5", context));
        assertEquals(HandlerType.SHOP, context.getLastSwitchedTo());

        assertTrue(handler.handleInput("6", context));
        assertEquals(GameState.Phase.QUIT, f.state.getPhase());
    }

    @Test
    void handleInput_shouldRejectInvalidChoice() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        MainMenuHandler handler = new MainMenuHandler();

        assertFalse(handler.handleInput("x", context));
    }
}
