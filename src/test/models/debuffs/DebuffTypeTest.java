package models.debuffs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DebuffTypeTest {

    @Test
    void enumContainsExpectedValues() {
        assertEquals(DebuffType.HUNGER_ENERGY, DebuffType.valueOf("HUNGER_ENERGY"));
        assertEquals(DebuffType.ENERGY_SKILL, DebuffType.valueOf("ENERGY_SKILL"));
        assertEquals(DebuffType.SOCIAL_FUN, DebuffType.valueOf("SOCIAL_FUN"));
        assertEquals(DebuffType.HYGIENE_SOCIAL, DebuffType.valueOf("HYGIENE_SOCIAL"));
        assertEquals(DebuffType.FATIGUE_DECAY, DebuffType.valueOf("FATIGUE_DECAY"));
        assertEquals(5, DebuffType.values().length);
    }
}