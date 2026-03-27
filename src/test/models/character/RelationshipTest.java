package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import types.RelationshipType;

class RelationshipTest {

    @Test
    void newRelationshipStartsNeutral() {
        Relationship relationship = new Relationship();

        assertEquals(0, relationship.getScore());
        assertEquals(RelationshipType.ACQUAINTANCE, relationship.getStatus());
    }

    @Test
    void adjustClampsToMaximum() {
        Relationship relationship = new Relationship();

        relationship.adjust(150);

        assertEquals(100, relationship.getScore());
        assertEquals(RelationshipType.BEST_FRIEND, relationship.getStatus());
    }

    @Test
    void adjustClampsToMinimum() {
        Relationship relationship = new Relationship();

        relationship.adjust(-200);

        assertEquals(-100, relationship.getScore());
        assertEquals(RelationshipType.ENEMY, relationship.getStatus());
    }

    @Test
    void adjustZeroLeavesRelationshipUnchanged() {
        Relationship relationship = new Relationship();

        relationship.adjust(0);

        assertEquals(0, relationship.getScore());
        assertEquals(RelationshipType.ACQUAINTANCE, relationship.getStatus());
    }

    @Test
    void adjustCrossesStatusThresholdsCorrectly() {
        Relationship relationship = new Relationship();

        relationship.adjust(25);
        assertEquals(RelationshipType.FRIENDLY, relationship.getStatus());

        relationship.adjust(25);
        assertEquals(RelationshipType.FRIEND, relationship.getStatus());
    }
}