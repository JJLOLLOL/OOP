package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import controller.CreateSimController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.GameState;

class RendererTest {

    @BeforeEach
    void setUp() {
        UITestSupport.resetRendererLayout();
    }

    @Test
    void menuHelpersReturnStyledMenuStrings() {
        assertEquals(Renderer.TITLE + "Menu" + ConsoleUtils.RESET, Renderer.menuTitle("Menu"));
        assertEquals(ConsoleUtils.BRIGHT_YELLOW + "1." + ConsoleUtils.RESET
                + " " + ConsoleUtils.WHITE + "Play" + ConsoleUtils.RESET, Renderer.menuItem("1", "Play"));
        assertEquals(Renderer.MUTED + "0. Back" + ConsoleUtils.RESET, Renderer.backItem());
    }

    @Test
    void showErrorPrintsMessageAndPrompt() throws Exception {
        String output = UITestSupport.captureOutput(() -> Renderer.showError("Invalid choice"));

        assertTrue(output.contains("[!]"));
        assertTrue(output.contains("Invalid choice"));
        assertTrue(output.endsWith("> "));
    }

    @Test
    void renderShowsCreateSimScreenWhenPhaseIsCreateSim() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        CreateSimController createSimController = new CreateSimController();

        String output = UITestSupport.withoutClearScreen(UITestSupport.captureOutput(
                () -> Renderer.render(fixture.state, fixture.world, createSimController)));

        assertTrue(output.contains("CREATE YOUR SIMS"));
        assertTrue(output.contains("How many Sims do you want to create?"));
    }

    @Test
    void renderShowsGameplayScreenWhenPhaseIsPlaying() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.state.setPhase(GameState.Phase.PLAYING);

        String output = UITestSupport.withoutClearScreen(UITestSupport.captureOutput(
                () -> Renderer.render(fixture.state, fixture.world, new CreateSimController())));

        assertTrue(output.contains("DAY 1"));
        assertTrue(output.contains("Actions"));
        assertTrue(output.contains("Skills"));
        assertTrue(output.contains("Notifications"));
    }

    @Test
    void renderOnlyClearsScreenWhenPhaseIsQuit() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        fixture.state.setPhase(GameState.Phase.QUIT);

        String output = UITestSupport.captureOutput(
                () -> Renderer.render(fixture.state, fixture.world, new CreateSimController()));

        assertEquals("\033[H\033[2J", output);
    }
}
