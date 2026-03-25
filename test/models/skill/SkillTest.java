package models.skill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void constructorRejectsInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> new Skill(null));
        assertThrows(IllegalArgumentException.class, () -> new Skill(SkillType.LOGIC, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Skill(SkillType.LOGIC, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> new Skill(SkillType.LOGIC, 1, 0));
    }

    @Test
    void defaultConstructorUsesExpectedValues() {
        Skill skill = new Skill(SkillType.LOGIC);

        assertEquals(SkillType.LOGIC, skill.getType());
        assertEquals("Logic", skill.getName());
        assertEquals(1, skill.getLevel());
        assertEquals(10, skill.getMaxLevel());
        assertEquals(0.0, skill.getProgress());
        assertEquals(100.0, skill.getRequiredXP());
    }

    @Test
    void addProgressCanLevelUpMultipleTimes() {
        Skill skill = new Skill(SkillType.PROGRAMMING, 1, 4);

        int levelsGained = skill.addProgress(260.0);

        assertEquals(2, levelsGained);
        assertEquals(3, skill.getLevel());
        assertEquals(10.0, skill.getProgress());
        assertEquals(225.0, skill.getRequiredXP());
    }

    @Test
    void addProgressStopsAtMaxLevelAndResetsXp() {
        Skill skill = new Skill(SkillType.FITNESS, 2, 3);

        int levelsGained = skill.addProgress(1000.0);

        assertEquals(1, levelsGained);
        assertEquals(3, skill.getLevel());
        assertTrue(skill.isMaxLevel());
        assertEquals(0.0, skill.getProgress());
    }

    @Test
    void addProgressReturnsZeroForZeroAmountAndAtMaxLevel() {
        Skill skill = new Skill(SkillType.COOKING, 2, 2);

        assertEquals(0, skill.addProgress(0.0));
        assertEquals(0, skill.addProgress(10.0));
        assertEquals(0.0, skill.getProgress());
    }

    @Test
    void toStringIncludesTypeLevelAndXp() {
        Skill skill = new Skill(SkillType.MUSIC);

        assertTrue(skill.toString().contains("MUSIC"));
        assertTrue(skill.toString().contains("Level: 1"));
        assertTrue(skill.toString().contains("XP: 0.0/100.0"));
    }
}
