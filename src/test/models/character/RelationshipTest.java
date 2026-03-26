package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import testTypes.RelationshipList;

class RelationshipTest {

    @Test
    void newRelationshipStartsNeutral() {
        Relationship relationship = new Relationship();

        assertEquals(0, relationship.getScore());
        assertEquals(RelationshipList.ACQUAINTANCE, relationship.getStatus());
    }

    @Test
    void adjustClampsToMaximum() {
        Relationship relationship = new Relationship();

        relationship.adjust(150);

        assertEquals(100, relationship.getScore());
        assertEquals(RelationshipList.BEST_FRIEND, relationship.getStatus());
    }

    @Test
    void adjustClampsToMinimum() {
        Relationship relationship = new Relationship();

        relationship.adjust(-200);

        assertEquals(-100, relationship.getScore());
        assertEquals(RelationshipList.ENEMY, relationship.getStatus());
    }

    @Test
    void adjustZeroLeavesRelationshipUnchanged() {
        Relationship relationship = new Relationship();

        relationship.adjust(0);

        assertEquals(0, relationship.getScore());
        assertEquals(RelationshipList.ACQUAINTANCE, relationship.getStatus());
    }

    @Test
    void adjustCrossesStatusThresholdsCorrectly() {
        Relationship relationship = new Relationship();

        relationship.adjust(25);
        assertEquals(RelationshipList.FRIENDLY, relationship.getStatus());

        relationship.adjust(25);
        assertEquals(RelationshipList.FRIEND, relationship.getStatus());
    }
}