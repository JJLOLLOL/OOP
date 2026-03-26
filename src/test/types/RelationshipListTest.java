package types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import testTypes.RelationshipList;

class RelationshipListTest {

    @Test
    void values_containsAllRelationshipTiersInOrder() {
        RelationshipList[] tiers = RelationshipList.values();

        assertEquals(6, tiers.length);
        assertEquals(RelationshipList.ENEMY, tiers[0]);
        assertEquals(RelationshipList.DISLIKED, tiers[1]);
        assertEquals(RelationshipList.ACQUAINTANCE, tiers[2]);
        assertEquals(RelationshipList.FRIENDLY, tiers[3]);
        assertEquals(RelationshipList.FRIEND, tiers[4]);
        assertEquals(RelationshipList.BEST_FRIEND, tiers[5]);
    }

    @Test
    void label_field_containsCorrectValues() {
        assertEquals("Enemy", RelationshipList.ENEMY.label);
        assertEquals("Disliked", RelationshipList.DISLIKED.label);
        assertEquals("Acquaintance", RelationshipList.ACQUAINTANCE.label);
        assertEquals("Friendly", RelationshipList.FRIENDLY.label);
        assertEquals("Friend", RelationshipList.FRIEND.label);
        assertEquals("Best Friend", RelationshipList.BEST_FRIEND.label);
    }

    @Test
    void from_returnsEnemyForEnemyRange() {
        assertEquals(RelationshipList.ENEMY, RelationshipList.from(-100));
        assertEquals(RelationshipList.ENEMY, RelationshipList.from(-75));
        assertEquals(RelationshipList.ENEMY, RelationshipList.from(-50));
    }

    @Test
    void from_returnsDislikedForDislikedRange() {
        assertEquals(RelationshipList.DISLIKED, RelationshipList.from(-49));
        assertEquals(RelationshipList.DISLIKED, RelationshipList.from(-30));
        assertEquals(RelationshipList.DISLIKED, RelationshipList.from(-25));
    }

    @Test
    void from_returnsAcquaintanceForAcquaintanceRange() {
        assertEquals(RelationshipList.ACQUAINTANCE, RelationshipList.from(-24));
        assertEquals(RelationshipList.ACQUAINTANCE, RelationshipList.from(0));
        assertEquals(RelationshipList.ACQUAINTANCE, RelationshipList.from(24));
    }

    @Test
    void from_returnsFriendlyForFriendlyRange() {
        assertEquals(RelationshipList.FRIENDLY, RelationshipList.from(25));
        assertEquals(RelationshipList.FRIENDLY, RelationshipList.from(40));
        assertEquals(RelationshipList.FRIENDLY, RelationshipList.from(49));
    }

    @Test
    void from_returnsFriendForFriendRange() {
        assertEquals(RelationshipList.FRIEND, RelationshipList.from(50));
        assertEquals(RelationshipList.FRIEND, RelationshipList.from(60));
        assertEquals(RelationshipList.FRIEND, RelationshipList.from(69));
    }

    @Test
    void from_returnsBestFriendForBestFriendRange() {
        assertEquals(RelationshipList.BEST_FRIEND, RelationshipList.from(70));
        assertEquals(RelationshipList.BEST_FRIEND, RelationshipList.from(85));
        assertEquals(RelationshipList.BEST_FRIEND, RelationshipList.from(100));
    }

    @Test
    void from_returnsAcquaintanceForScoresOutsideDefinedRanges() {
        assertEquals(RelationshipList.ACQUAINTANCE, RelationshipList.from(-101));
        assertEquals(RelationshipList.ACQUAINTANCE, RelationshipList.from(101));
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(RelationshipList.ENEMY, RelationshipList.valueOf("ENEMY"));
        assertEquals(RelationshipList.BEST_FRIEND, RelationshipList.valueOf("BEST_FRIEND"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> RelationshipList.valueOf("INVALID"));
    }
}