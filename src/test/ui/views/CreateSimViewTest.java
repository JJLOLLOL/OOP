package ui.views;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.CreateSimController;
import ui.UITestSupport;

class CreateSimViewTest {

    @BeforeEach
    void setUp() {
        UITestSupport.resetRendererLayout();
    }

    @Test
    void renderCountStepShowsInitialPrompt() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        CreateSimController controller = new CreateSimController();

        String output = UITestSupport.captureOutput(() -> CreateSimView.render(fixture.state, controller));

        assertTrue(output.contains("CREATE YOUR SIMS"));
        assertTrue(output.contains("How many Sims do you want to create?"));
        assertTrue(output.endsWith("\n> "));
    }

    @Test
    void renderNameStepShowsCommittedSimsAndCurrentIndex() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        CreateSimController controller = new CreateSimController();
        controller.handleInput("2", fixture.state, fixture.world);
        controller.handleInput("Chris", fixture.state, fixture.world);
        controller.handleInput("31", fixture.state, fixture.world);
        controller.handleInput("M", fixture.state, fixture.world);

        String output = UITestSupport.captureOutput(() -> CreateSimView.render(fixture.state, controller));

        assertTrue(output.contains("Sims added so far:"));
        assertTrue(output.contains("Chris"));
        assertTrue(output.contains("Creating Sim 2 of 2"));
        assertTrue(output.contains("Enter name:"));
    }

    @Test
    void renderGenderStepShowsCollectedFields() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        CreateSimController controller = new CreateSimController();
        controller.handleInput("1", fixture.state, fixture.world);
        controller.handleInput("Alex", fixture.state, fixture.world);
        controller.handleInput("25", fixture.state, fixture.world);

        String output = UITestSupport.captureOutput(() -> CreateSimView.render(fixture.state, controller));

        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Alex"));
        assertTrue(output.contains("Age"));
        assertTrue(output.contains("25"));
        assertTrue(output.contains("Enter gender (M / F):"));
    }

    @Test
    void renderConfirmStepShowsReviewAndConfirmationPrompt() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        CreateSimController controller = new CreateSimController();
        controller.handleInput("2", fixture.state, fixture.world);
        controller.handleInput("Alex", fixture.state, fixture.world);
        controller.handleInput("25", fixture.state, fixture.world);
        controller.handleInput("M", fixture.state, fixture.world);
        controller.handleInput("Jamie", fixture.state, fixture.world);
        controller.handleInput("22", fixture.state, fixture.world);
        controller.handleInput("F", fixture.state, fixture.world);

        String output = UITestSupport.captureOutput(() -> CreateSimView.render(fixture.state, controller));

        assertTrue(output.contains("Review your Sims:"));
        assertTrue(output.contains("Alex"));
        assertTrue(output.contains("Jamie"));
        assertTrue(output.contains("Confirm?"));
    }

    @Test
    void renderPickPlayerStepListsCreatedSims() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        CreateSimController controller = new CreateSimController();
        controller.handleInput("2", fixture.state, fixture.world);
        controller.handleInput("Alex", fixture.state, fixture.world);
        controller.handleInput("25", fixture.state, fixture.world);
        controller.handleInput("M", fixture.state, fixture.world);
        controller.handleInput("Jamie", fixture.state, fixture.world);
        controller.handleInput("22", fixture.state, fixture.world);
        controller.handleInput("F", fixture.state, fixture.world);
        controller.handleInput("Y", fixture.state, fixture.world);

        String output = UITestSupport.captureOutput(() -> CreateSimView.render(fixture.state, controller));

        assertTrue(output.contains("Choose your active Sim:"));
        assertTrue(output.contains("Alex"));
        assertTrue(output.contains("Jamie"));
    }
}
