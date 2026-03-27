package ui.panels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import services.NotificationService;
import ui.ConsoleUtils;
import ui.Renderer;
import ui.UITestSupport;

class NotificationsPanelViewTest {

    @BeforeEach
    void setUp() {
        UITestSupport.resetRendererLayout();
    }

    @Test
    void buildShowsNoneWhenPlayerHasNoNotifications() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        List<String> lines = NotificationsPanelView.build(fixture.player);

        assertEquals("Notifications", UITestSupport.plain(lines.get(0)));
        assertEquals("None.", UITestSupport.plain(lines.get(1)));
    }

    @Test
    void buildPrioritisesAchievementNotifications() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        Renderer.NOTIF_W = 80;
        NotificationService.add(fixture.player, "You paid rent.");
        NotificationService.add(fixture.player, "Achievement unlocked: First Job");

        List<String> lines = NotificationsPanelView.build(fixture.player);

        assertEquals("Achievement unlocked: First Job", UITestSupport.plain(lines.get(1)));
        assertEquals("You paid rent.", UITestSupport.plain(lines.get(3)));
    }

    @Test
    void buildWrapsMessagesAndClassifiesSeverityByColour() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        Renderer.NOTIF_W = 20;

        NotificationService.add(fixture.player, "Levelled up in Programming");
        NotificationService.add(fixture.player, "Cannot continue, not enough money");
        NotificationService.add(fixture.player, "Warning: low hygiene cost");

        List<String> lines = NotificationsPanelView.build(fixture.player);

        assertTrue(UITestSupport.findLineContaining(lines, "Levelled up").contains(ConsoleUtils.BRIGHT_GREEN));
        assertTrue(UITestSupport.findLineContaining(lines, "enough money").contains(ConsoleUtils.BRIGHT_RED));
        assertTrue(UITestSupport.findLineContaining(lines, "Warning: low").contains(ConsoleUtils.BRIGHT_YELLOW));
        assertFalse(UITestSupport.plain(lines.get(lines.size() - 1)).isBlank());
    }
}
