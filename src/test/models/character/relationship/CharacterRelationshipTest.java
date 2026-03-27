package models.character.relationship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import models.character.Character;
import models.location.Location;
import types.Gender;
import types.RelationshipList;

import org.junit.jupiter.api.Test;

class CharacterRelationshipTest {

    private static class TestCharacter extends Character {
        TestCharacter(String name) {
            super(name, 20, Gender.MALE, new Location("Home", new ArrayList<>()));
        }
    }

    @Test
    void constructorRejectsNullOwner() {
        assertThrows(IllegalArgumentException.class, () -> new CharacterRelationship(null));
    }

    @Test
    void initializeWithRejectsNullAndSelf() {
        TestCharacter alex = new TestCharacter("Alex");

        assertThrows(IllegalArgumentException.class, () -> alex.getRelationships().initializeWith(null));
        assertThrows(IllegalArgumentException.class, () -> alex.getRelationships().initializeWith(alex));
    }

    @Test
    void initializeWithCreatesSharedRelationshipOnBothSides() {
        TestCharacter alex = new TestCharacter("Alex");
        TestCharacter sam = new TestCharacter("Sam");

        alex.getRelationships().initializeWith(sam);

        assertEquals(1, alex.getRelationships().getRelationshipViews().size());
        assertEquals(1, sam.getRelationships().getRelationshipViews().size());
        assertEquals(0, alex.getRelationships().getScoreWith(sam));
        assertEquals(0, sam.getRelationships().getScoreWith(alex));
    }

    @Test
    void changeRelationshipWithAutoInitializesAndUpdatesBothSides() {
        TestCharacter alex = new TestCharacter("Alex");
        TestCharacter sam = new TestCharacter("Sam");

        alex.getRelationships().changeRelationshipWith(sam, 50);

        assertEquals(50, alex.getRelationships().getScoreWith(sam));
        assertEquals(50, sam.getRelationships().getScoreWith(alex));
        assertEquals(RelationshipList.FRIEND, alex.getRelationships().getStatusWith(sam));
        assertEquals(RelationshipList.FRIEND, sam.getRelationships().getStatusWith(alex));
    }

    @Test
    void unknownRelationshipReturnsDefaultValues() {
        TestCharacter alex = new TestCharacter("Alex");
        TestCharacter sam = new TestCharacter("Sam");

        assertEquals(0, alex.getRelationships().getScoreWith(sam));
        assertEquals(RelationshipList.ACQUAINTANCE, alex.getRelationships().getStatusWith(sam));
    }

    @Test
    void accessorsRejectNullTarget() {
        TestCharacter alex = new TestCharacter("Alex");

        assertThrows(IllegalArgumentException.class, () -> alex.getRelationships().changeRelationshipWith(null, 10));
        assertThrows(IllegalArgumentException.class, () -> alex.getRelationships().getScoreWith(null));
        assertThrows(IllegalArgumentException.class, () -> alex.getRelationships().getStatusWith(null));
    }
}