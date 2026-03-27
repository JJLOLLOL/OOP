package ui.views;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import services.NotificationService;
import ui.UITestSupport;

class GameplayViewTest {

    @BeforeEach
    void setUp() {
        UITestSupport.resetRendererLayout();
    }

    @Test
    void renderBuildsFourPanelGameplayScreen() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        NotificationService.add(fixture.player, "Warning: low hygiene cost");

        String output = UITestSupport.captureOutput(
                () -> GameplayView.render(fixture.state, fixture.world, fixture.playController));

        assertTrue(output.contains("DAY 1"));
        assertTrue(output.contains("Actions"));
        assertTrue(output.contains("Skills"));
        assertTrue(output.contains("Notifications"));
        assertTrue(output.contains("Alex"));
        assertTrue(output.contains("Warning: low hygiene cost"));
        assertTrue(output.endsWith("\n> "));
    }
}
