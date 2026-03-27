// src/test/controller/play/LocationChangeHandlerTest.java
package controller.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ui.UITestSupport;

class LocationChangeHandlerTest {

    @Test
    void handleInput_shouldGoBackOnZero() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        LocationChangeHandler handler = new LocationChangeHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("0", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldChangeLocationOnValidSelection() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        f.player.setLocation(f.home);
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        LocationChangeHandler handler = new LocationChangeHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("2", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
        assertTrue(!"Home".equals(f.player.getLocation().getLocationName()));
    }

    @Test
    void handleInput_shouldRejectInvalidSelection() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        LocationChangeHandler handler = new LocationChangeHandler();
        handler.onEnter(context);

        assertFalse(handler.handleInput("99", context));
    }
}