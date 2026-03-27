package types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RelationshipListTest {

    @Test
    void values_containsAllRelationshipTiersInOrder() {
        RelationshipType[] tiers = RelationshipType.values();

        assertEquals(6, tiers.length);
        assertEquals(RelationshipType.ENEMY, tiers[0]);
        assertEquals(RelationshipType.DISLIKED, tiers[1]);
        assertEquals(RelationshipType.ACQUAINTANCE, tiers[2]);
        assertEquals(RelationshipType.FRIENDLY, tiers[3]);
        assertEquals(RelationshipType.FRIEND, tiers[4]);
        assertEquals(RelationshipType.BEST_FRIEND, tiers[5]);
    }

    @Test
    void label_field_containsCorrectValues() {
        assertEquals("Enemy", RelationshipType.ENEMY.label);
        assertEquals("Disliked", RelationshipType.DISLIKED.label);
        assertEquals("Acquaintance", RelationshipType.ACQUAINTANCE.label);
        assertEquals("Friendly", RelationshipType.FRIENDLY.label);
        assertEquals("Friend", RelationshipType.FRIEND.label);
        assertEquals("Best Friend", RelationshipType.BEST_FRIEND.label);
    }

    @Test
    void from_returnsEnemyForEnemyRange() {
        assertEquals(RelationshipType.ENEMY, RelationshipType.from(-100));
        assertEquals(RelationshipType.ENEMY, RelationshipType.from(-75));
        assertEquals(RelationshipType.ENEMY, RelationshipType.from(-50));
    }

    @Test
    void from_returnsDislikedForDislikedRange() {
        assertEquals(RelationshipType.DISLIKED, RelationshipType.from(-49));
        assertEquals(RelationshipType.DISLIKED, RelationshipType.from(-30));
        assertEquals(RelationshipType.DISLIKED, RelationshipType.from(-25));
    }

    @Test
    void from_returnsAcquaintanceForAcquaintanceRange() {
        assertEquals(RelationshipType.ACQUAINTANCE, RelationshipType.from(-24));
        assertEquals(RelationshipType.ACQUAINTANCE, RelationshipType.from(0));
        assertEquals(RelationshipType.ACQUAINTANCE, RelationshipType.from(24));
    }

    @Test
    void from_returnsFriendlyForFriendlyRange() {
        assertEquals(RelationshipType.FRIENDLY, RelationshipType.from(25));
        assertEquals(RelationshipType.FRIENDLY, RelationshipType.from(40));
        assertEquals(RelationshipType.FRIENDLY, RelationshipType.from(49));
    }

    @Test
    void from_returnsFriendForFriendRange() {
        assertEquals(RelationshipType.FRIEND, RelationshipType.from(50));
        assertEquals(RelationshipType.FRIEND, RelationshipType.from(60));
        assertEquals(RelationshipType.FRIEND, RelationshipType.from(69));
    }

    @Test
    void from_returnsBestFriendForBestFriendRange() {
        assertEquals(RelationshipType.BEST_FRIEND, RelationshipType.from(70));
        assertEquals(RelationshipType.BEST_FRIEND, RelationshipType.from(85));
        assertEquals(RelationshipType.BEST_FRIEND, RelationshipType.from(100));
    }

    @Test
    void from_returnsAcquaintanceForScoresOutsideDefinedRanges() {
        assertEquals(RelationshipType.ACQUAINTANCE, RelationshipType.from(-101));
        assertEquals(RelationshipType.ACQUAINTANCE, RelationshipType.from(101));
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(RelationshipType.ENEMY, RelationshipType.valueOf("ENEMY"));
        assertEquals(RelationshipType.BEST_FRIEND, RelationshipType.valueOf("BEST_FRIEND"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> RelationshipType.valueOf("INVALID"));
    }
}