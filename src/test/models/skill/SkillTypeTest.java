package models.skill;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SkillTypeTest {

    @Test
    void values_containsAllSkillTypesInOrder() {
        SkillType[] types = SkillType.values();

        assertEquals(9, types.length);
        assertEquals(SkillType.COOKING, types[0]);
        assertEquals(SkillType.FITNESS, types[1]);
        assertEquals(SkillType.PROGRAMMING, types[2]);
        assertEquals(SkillType.CHARISMA, types[3]);
        assertEquals(SkillType.CREATIVITY, types[4]);
        assertEquals(SkillType.LOGIC, types[5]);
        assertEquals(SkillType.MUSIC, types[6]);
        assertEquals(SkillType.WRITING, types[7]);
        assertEquals(SkillType.PAINTING, types[8]);
    }

    @Test
    void getName_returnsCorrectDisplayNames() {
        assertEquals("Cooking", SkillType.COOKING.getName());
        assertEquals("Fitness", SkillType.FITNESS.getName());
        assertEquals("Programming", SkillType.PROGRAMMING.getName());
        assertEquals("Charisma", SkillType.CHARISMA.getName());
        assertEquals("Creativity", SkillType.CREATIVITY.getName());
        assertEquals("Logic", SkillType.LOGIC.getName());
        assertEquals("Music", SkillType.MUSIC.getName());
        assertEquals("Writing", SkillType.WRITING.getName());
        assertEquals("Painting", SkillType.PAINTING.getName());
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(SkillType.COOKING, SkillType.valueOf("COOKING"));
        assertEquals(SkillType.FITNESS, SkillType.valueOf("FITNESS"));
        assertEquals(SkillType.PROGRAMMING, SkillType.valueOf("PROGRAMMING"));
        assertEquals(SkillType.CHARISMA, SkillType.valueOf("CHARISMA"));
        assertEquals(SkillType.CREATIVITY, SkillType.valueOf("CREATIVITY"));
        assertEquals(SkillType.LOGIC, SkillType.valueOf("LOGIC"));
        assertEquals(SkillType.MUSIC, SkillType.valueOf("MUSIC"));
        assertEquals(SkillType.WRITING, SkillType.valueOf("WRITING"));
        assertEquals(SkillType.PAINTING, SkillType.valueOf("PAINTING"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> SkillType.valueOf("INVALID"));
    }
}