package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Types.RelationshipList;
import org.junit.jupiter.api.Test;

class RelationshipTest {

    @Test
    void newRelationshipStartsNeutral() {
        Relationship relationship = new Relationship();

        assertEquals(0, relationship.getScore());
        assertEquals(RelationshipList.ACQUAINTANCE, relationship.getStatus());
    }

    @Test
    void changeScoreClampsToConfiguredMaximum() {
        Relationship relationship = new Relationship();

        relationship.changeScore(150);

        assertEquals(100, relationship.getScore());
        assertEquals(RelationshipList.BEST_FRIEND, relationship.getStatus());
    }

    @Test
    void changeScoreClampsToConfiguredMinimum() {
        Relationship relationship = new Relationship();

        relationship.changeScore(-200);

        assertEquals(-100, relationship.getScore());
        assertEquals(RelationshipList.ENEMY, relationship.getStatus());
    }

    @Test
    void changeScoreUpdatesTierAcrossThresholds() {
        Relationship relationship = new Relationship();

        relationship.changeScore(25);
        assertEquals(RelationshipList.FRIENDLY, relationship.getStatus());

        relationship.changeScore(25);
        assertEquals(RelationshipList.FRIEND, relationship.getStatus());
    }
}
