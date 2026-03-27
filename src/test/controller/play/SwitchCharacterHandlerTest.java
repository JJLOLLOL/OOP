package controller.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ui.UITestSupport;

class SwitchCharacterHandlerTest {

    @Test
    void handleInput_shouldGoBackOnZero() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SwitchCharacterHandler handler = new SwitchCharacterHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("0", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldSwitchActivePlayer() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SwitchCharacterHandler handler = new SwitchCharacterHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("2", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
        assertEquals(f.roommate, f.state.getActivePlayer());
    }

    @Test
    void handleInput_shouldRejectInvalidChoice() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SwitchCharacterHandler handler = new SwitchCharacterHandler();
        handler.onEnter(context);

        assertFalse(handler.handleInput("99", context));
    }
}