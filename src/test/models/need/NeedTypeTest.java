package models.need;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NeedTypeTest {

    @Test
    void values_containsAllNeedTypesInOrder() {
        NeedType[] types = NeedType.values();

        assertEquals(5, types.length);
        assertEquals(NeedType.HUNGER, types[0]);
        assertEquals(NeedType.HYGIENE, types[1]);
        assertEquals(NeedType.ENERGY, types[2]);
        assertEquals(NeedType.FUN, types[3]);
        assertEquals(NeedType.SOCIAL, types[4]);
    }

    @Test
    void getName_returnsCorrectDisplayNames() {
        assertEquals("Hunger", NeedType.HUNGER.getName());
        assertEquals("Hygiene", NeedType.HYGIENE.getName());
        assertEquals("Energy", NeedType.ENERGY.getName());
        assertEquals("Fun", NeedType.FUN.getName());
        assertEquals("Social", NeedType.SOCIAL.getName());
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(NeedType.HUNGER, NeedType.valueOf("HUNGER"));
        assertEquals(NeedType.HYGIENE, NeedType.valueOf("HYGIENE"));
        assertEquals(NeedType.ENERGY, NeedType.valueOf("ENERGY"));
        assertEquals(NeedType.FUN, NeedType.valueOf("FUN"));
        assertEquals(NeedType.SOCIAL, NeedType.valueOf("SOCIAL"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> NeedType.valueOf("INVALID"));
    }
}