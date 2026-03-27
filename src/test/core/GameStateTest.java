package core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ui.UITestSupport;

class GameStateTest {

    @Test
    void constructorCreatesRunningStateWithSharedServices() {
        GameState state = new GameState();

        assertSame(GameState.Phase.CREATE_SIM, state.getPhase());
        assertTrue(state.isRunning());
        assertNotNull(state.getRelationshipService());
        assertNotNull(state.getAchievementService());
        assertNotNull(state.getGameClock());
    }

    @Test
    void addSimAndSetActivePlayerStoreCharacters() {
        GameState state = new GameState();
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        state.addSim(fixture.player);
        state.addSim(fixture.roommate);
        state.setActivePlayer(fixture.roommate);

        assertSame(fixture.player, state.getSims().get(0));
        assertSame(fixture.roommate, state.getSims().get(1));
        assertSame(fixture.roommate, state.getActivePlayer());
    }
}
