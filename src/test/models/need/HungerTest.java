package models.need;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HungerTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Hunger hunger = new Hunger();

        assertEquals(NeedType.HUNGER, hunger.getType());
        assertEquals("Hunger", hunger.getNeedName());
        assertEquals(80.0, hunger.getValue());
        assertEquals(8.0, hunger.getDecayRate());
        assertEquals(8.0, hunger.getBaseDecayRate());
    }
}