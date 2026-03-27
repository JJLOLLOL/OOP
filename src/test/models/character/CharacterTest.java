package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import models.location.Location;
import types.Gender;
import types.RelationshipType;

import org.junit.jupiter.api.Test;

class CharacterTest {

    private static class TestCharacter extends Character {
        TestCharacter(String name, int age, Gender gender, Location location) {
            super(name, age, gender, location);
        }
    }

    @Test
    void constructorStoresCoreFieldsAndLocationCanChange() {
        Location home = new Location("Home", new ArrayList<>());
        Location park = new Location("Park", new ArrayList<>());
        TestCharacter character = new TestCharacter("Alex", 30, Gender.MALE, home);

        assertEquals("Alex", character.getName());
        assertEquals(30, character.getAge());
        assertEquals(Gender.MALE, character.getGender());
        assertSame(home, character.getLocation());

        character.setLocation(park);

        assertSame(park, character.getLocation());
    }

    @Test
    void relationshipMethodsDelegateToCharacterRelationship() {
        Location home = new Location("Home", new ArrayList<>());
        TestCharacter alex = new TestCharacter("Alex", 30, Gender.MALE, home);
        TestCharacter sam = new TestCharacter("Sam", 28, Gender.FEMALE, home);

        alex.initializeRelationshipWith(sam);
        alex.changeRelationshipWith(sam, 30);

        assertEquals(30, alex.getRelationshipScoreWith(sam));
        assertEquals(30, sam.getRelationshipScoreWith(alex));
        assertEquals(RelationshipType.FRIENDLY, alex.getRelationshipStatus(sam));
        assertEquals(RelationshipType.FRIENDLY, sam.getRelationshipStatus(alex));
    }
}