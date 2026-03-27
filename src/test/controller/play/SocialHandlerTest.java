// src/test/controller/play/SocialHandlerTest.java
package controller.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import models.need.NeedType;
import services.NotificationService;
import ui.UITestSupport;

class SocialHandlerTest {

    @Test
    void handleInput_shouldGoBackFromCharacterList() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SocialHandler handler = new SocialHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("0", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
    }

    @Test
    void handleInput_shouldSelectCharacterAndGoBackFromActionMenu() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SocialHandler handler = new SocialHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertEquals(controller.PlayController.Step.SOCIALISE_ACTION, handler.getStep());
        assertTrue(handler.getSelectedCharacter() != null);

        assertTrue(handler.handleInput("0", context));
        assertEquals(controller.PlayController.Step.SOCIALISE, handler.getStep());
        assertNull(handler.getSelectedCharacter());
    }

    @Test
    void handleInput_shouldBlockInteractionWhenDebuffed() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        f.player.adjustNeed(NeedType.HYGIENE, -100.0);
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SocialHandler handler = new SocialHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertTrue(handler.handleInput("1", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
        assertTrue(NotificationService.get(f.player).stream().anyMatch(m -> m.contains("refused to interact")));
    }

    @Test
    void handleInput_shouldPerformInteractionWhenAllowed() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        int before = f.player.getRelationshipScoreWith(f.roommate);
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SocialHandler handler = new SocialHandler();
        handler.onEnter(context);

        assertTrue(handler.handleInput("1", context));
        assertTrue(handler.handleInput("1", context));
        assertEquals(HandlerType.MAIN_MENU, context.getLastSwitchedTo());
        assertTrue(f.player.getRelationshipScoreWith(f.roommate) > before);
    }

    @Test
    void handleInput_shouldRejectInvalidSelections() {
        UITestSupport.Fixture f = UITestSupport.fixture();
        PlayContextTest context = new PlayContextTest(f.state, f.world, f.shopInventory);
        SocialHandler handler = new SocialHandler();
        handler.onEnter(context);

        assertFalse(handler.handleInput("99", context));
        assertTrue(handler.handleInput("1", context));
        assertFalse(handler.handleInput("99", context));
    }
}