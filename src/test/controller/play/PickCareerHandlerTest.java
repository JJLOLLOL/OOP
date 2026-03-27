package controller.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ui.UITestSupport;

class PickCareerHandlerTest {

    @Test
    void handleInput_shouldGoBackOnZero() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        PickCareerHandler handler = new PickCareerHandler();

        assertTrue(handler.handleInput("0", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldJoinCareerOnValidChoice() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        PickCareerHandler handler = new PickCareerHandler();

        assertTrue(handler.handleInput("1", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
        assertFalse(f.player.isJobless());
    }

    @Test
    void handleInput_shouldRejectInvalidChoice() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        PickCareerHandler handler = new PickCareerHandler();

        assertFalse(handler.handleInput("99", context));
    }
}