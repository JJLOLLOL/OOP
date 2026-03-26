package models.need;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HygieneTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Hygiene hygiene = new Hygiene();

        assertEquals(NeedType.HYGIENE, hygiene.getType());
        assertEquals("Hygiene", hygiene.getNeedName());
        assertEquals(80.0, hygiene.getValue());
        assertEquals(3.0, hygiene.getDecayRate());
        assertEquals(3.0, hygiene.getBaseDecayRate());
    }
}