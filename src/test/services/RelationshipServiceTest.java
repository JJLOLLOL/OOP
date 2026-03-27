package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import types.InteractionType;
import ui.UITestSupport;

class RelationshipServiceTest {

    @Test
    void registerNewSimInitialisesRelationshipsWithExistingSimsAndNpcs() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        RelationshipService service = new RelationshipService();

        service.registerNewSim(fixture.player, fixture.state.getSims(), fixture.world.getAllNPCs());

        assertEquals(0, fixture.player.getRelationshipScoreWith(fixture.roommate));
        assertEquals(0, fixture.roommate.getRelationshipScoreWith(fixture.player));
        assertEquals(0, fixture.player.getRelationshipScoreWith(fixture.npc));
    }

    @Test
    void interactChangesSharedRelationshipAndReturnsSummary() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        RelationshipService service = new RelationshipService();

        String result = service.interact(fixture.player, fixture.roommate, InteractionType.COMPLIMENT);

        assertEquals(10, fixture.player.getRelationshipScoreWith(fixture.roommate));
        assertEquals(10, fixture.roommate.getRelationshipScoreWith(fixture.player));
        assertTrue(result.contains("Alex Compliment Jamie"));
        assertTrue(result.contains("Relationship with Jamie improved to 10"));
    }
}
