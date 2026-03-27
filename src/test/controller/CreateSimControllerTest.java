package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import core.GameState;
import ui.UITestSupport;

class CreateSimControllerTest {

    @Test
    void handleInputAdvancesThroughCreationStepsAndCommitsSimData() {
        CreateSimController controller = new CreateSimController();
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        assertTrue(controller.handleInput("2", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.NAME, controller.getStep());

        assertTrue(controller.handleInput("Alex", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.AGE, controller.getStep());
        assertEquals("Alex", controller.getInFlightName());

        assertTrue(controller.handleInput("25", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.GENDER, controller.getStep());
        assertEquals("25", controller.getInFlightAge());

        assertTrue(controller.handleInput("M", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.NAME, controller.getStep());
        assertEquals(2, controller.getCommitted().size());
        assertEquals(1, controller.getCurrentIndex());
        assertEquals("Alex", controller.getCommitted().get(0).getName());
        assertEquals(25, controller.getCommitted().get(0).getAge());
        assertEquals("M", controller.getCommitted().get(0).getGenderLabel());
    }

    @Test
    void invalidInputsReturnFalseAndLeaveControllerOnSameStep() {
        CreateSimController controller = new CreateSimController();
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        assertFalse(controller.handleInput("0", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.COUNT, controller.getStep());

        controller.handleInput("1", fixture.state, fixture.world);
        assertFalse(controller.handleInput("   ", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.NAME, controller.getStep());

        controller.handleInput("Alex", fixture.state, fixture.world);
        assertFalse(controller.handleInput("9", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.AGE, controller.getStep());

        controller.handleInput("25", fixture.state, fixture.world);
        assertFalse(controller.handleInput("X", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.GENDER, controller.getStep());
    }

    @Test
    void confirmingSingleSimStartsPlayingAndAssignsHome() {
        CreateSimController controller = new CreateSimController();
        GameState state = new GameState();
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        controller.handleInput("1", state, fixture.world);
        controller.handleInput("Solo", state, fixture.world);
        controller.handleInput("30", state, fixture.world);
        controller.handleInput("F", state, fixture.world);
        assertEquals(CreateSimController.Step.CONFIRM, controller.getStep());

        assertTrue(controller.handleInput("Y", state, fixture.world));
        assertSame(GameState.Phase.PLAYING, state.getPhase());
        assertEquals(1, state.getSims().size());
        assertSame(state.getSims().get(0), state.getActivePlayer());
        assertSame(fixture.home, state.getActivePlayer().getCurrentHouse());
    }

    @Test
    void confirmingMultipleSimsMovesToPickPlayerAndSelectionStartsGame() {
        CreateSimController controller = new CreateSimController();
        GameState state = new GameState();
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        controller.handleInput("2", state, fixture.world);
        controller.handleInput("Alex", state, fixture.world);
        controller.handleInput("25", state, fixture.world);
        controller.handleInput("M", state, fixture.world);
        controller.handleInput("Jamie", state, fixture.world);
        controller.handleInput("22", state, fixture.world);
        controller.handleInput("F", state, fixture.world);

        assertTrue(controller.handleInput("yes", state, fixture.world));
        assertEquals(CreateSimController.Step.PICK_PLAYER, controller.getStep());
        assertEquals(2, state.getSims().size());
        assertEquals(0, state.getSims().get(0).getRelationshipScoreWith(state.getSims().get(1)));

        assertTrue(controller.handleInput("2", state, fixture.world));
        assertSame(GameState.Phase.PLAYING, state.getPhase());
        assertEquals("Jamie", state.getActivePlayer().getName());
    }

    @Test
    void decliningConfirmationResetsFlowToCount() {
        CreateSimController controller = new CreateSimController();
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        controller.handleInput("1", fixture.state, fixture.world);
        controller.handleInput("Alex", fixture.state, fixture.world);
        controller.handleInput("25", fixture.state, fixture.world);
        controller.handleInput("M", fixture.state, fixture.world);

        assertTrue(controller.handleInput("n", fixture.state, fixture.world));
        assertEquals(CreateSimController.Step.COUNT, controller.getStep());
        assertEquals(0, controller.getCommitted().size());
        assertEquals(0, controller.getCurrentIndex());
    }
}
