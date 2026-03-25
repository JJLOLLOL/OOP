package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import models.location.Location;
import org.junit.jupiter.api.Test;

class CharacterTest {

    private static class TestCharacter extends Character {
        TestCharacter(String name, int age, String gender, Location location) {
            super(name, age, gender, location);
        }
    }

    @Test
    void characterStoresCoreFieldsAndLocationCanChange() {
        Location home = new Location("Home", new ArrayList<>());
        Location park = new Location("Park", new ArrayList<>());
        TestCharacter character = new TestCharacter("Alex", 30, "M", home);

        assertEquals("Alex", character.getName());
        assertEquals(30, character.getAge());
        assertEquals("M", character.getGender());
        assertEquals(home, character.getLocation());

        character.setLocation(park);
        assertEquals(park, character.getLocation());
    }
}
