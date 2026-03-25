package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NeedTypeTest {

    @Test
    void getTypeAcceptsEnumName() {
        assertEquals(NeedType.ENERGY, NeedType.getType("ENERGY"));
        assertEquals("Energy", NeedType.ENERGY.getName());
    }

    @Test
    void getTypeRejectsDisplayNameAndInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> NeedType.getType("Energy"));
        assertThrows(IllegalArgumentException.class, () -> NeedType.getType("UNKNOWN"));
    }
}
