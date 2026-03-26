package models.need;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FunTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Fun fun = new Fun();

        assertEquals(NeedType.FUN, fun.getType());
        assertEquals("Fun", fun.getNeedName());
        assertEquals(80.0, fun.getValue());
        assertEquals(3.0, fun.getDecayRate());
        assertEquals(3.0, fun.getBaseDecayRate());
    }
}