package models.skill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SkillTypeTest {

    @Test
    void getTypeAcceptsEnumName() {
        assertEquals(SkillType.COOKING, SkillType.getType("COOKING"));
        assertEquals("Cooking", SkillType.COOKING.getName());
    }

    @Test
    void getTypeRejectsDisplayNameAndInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> SkillType.getType("Cooking"));
        assertThrows(IllegalArgumentException.class, () -> SkillType.getType("UNKNOWN"));
    }
}
