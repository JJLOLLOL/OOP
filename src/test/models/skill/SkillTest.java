package models.skill;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void defaultConstructor_setsDefaultValues() {
        Skill skill = new Skill(SkillType.COOKING);

        assertEquals(SkillType.COOKING, skill.getType());
        assertEquals("Cooking", skill.getName());
        assertEquals(1, skill.getLevel());
        assertEquals(10, skill.getMaxLevel());
        assertEquals(0.0, skill.getProgress());
        assertEquals(100.0, skill.getRequiredXP());
        assertFalse(skill.isMaxLevel());
    }

    @Test
    void fullConstructor_setsProvidedValues() {
        Skill skill = new Skill(SkillType.LOGIC, 3, 8);

        assertEquals(SkillType.LOGIC, skill.getType());
        assertEquals("Logic", skill.getName());
        assertEquals(3, skill.getLevel());
        assertEquals(8, skill.getMaxLevel());
        assertEquals(0.0, skill.getProgress());
        assertEquals(225.0, skill.getRequiredXP());
        assertFalse(skill.isMaxLevel());
    }

    @Test
    void constructor_throwsExceptionWhenTypeIsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> new Skill(null));

        assertEquals("Skill type cannot be null.", ex.getMessage());
    }

    @Test
    void constructor_throwsExceptionWhenStartingLevelIsLessThanOne() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> new Skill(SkillType.MUSIC, 0, 10));

        assertEquals("Starting level must be at least 1.", ex.getMessage());
    }

    @Test
    void constructor_throwsExceptionWhenMaxLevelIsInvalid() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> new Skill(SkillType.MUSIC, 3, 2));

        assertEquals("Invalid max level.", ex.getMessage());
    }

    @Test
    void constructor_throwsExceptionWhenMaxLevelIsLessThanOne() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> new Skill(SkillType.MUSIC, 1, 0));

        assertEquals("Invalid max level.", ex.getMessage());
    }

    @Test
    void addProgress_returnsZeroWhenAmountIsZero() {
        Skill skill = new Skill(SkillType.FITNESS);

        int levelsGained = skill.addProgress(0);

        assertEquals(0, levelsGained);
        assertEquals(1, skill.getLevel());
        assertEquals(0.0, skill.getProgress());
    }

    @Test
    void addProgress_addsXpWithoutLevelUpWhenNotEnough() {
        Skill skill = new Skill(SkillType.FITNESS);

        int levelsGained = skill.addProgress(40);

        assertEquals(0, levelsGained);
        assertEquals(1, skill.getLevel());
        assertEquals(40.0, skill.getProgress());
        assertEquals(100.0, skill.getRequiredXP());
    }

    @Test
    void addProgress_levelsUpOnceAndCarriesOverRemainingXp() {
        Skill skill = new Skill(SkillType.PROGRAMMING);

        int levelsGained = skill.addProgress(120);

        assertEquals(1, levelsGained);
        assertEquals(2, skill.getLevel());
        assertEquals(20.0, skill.getProgress());
        assertEquals(150.0, skill.getRequiredXP());
    }

    @Test
    void addProgress_canLevelUpMultipleTimes() {
        Skill skill = new Skill(SkillType.CHARISMA);

        int levelsGained = skill.addProgress(300);

        assertEquals(2, levelsGained);
        assertEquals(3, skill.getLevel());
        assertEquals(50.0, skill.getProgress());
        assertEquals(225.0, skill.getRequiredXP());
    }

    @Test
    void addProgress_doesNotDropBelowZeroWhenNegativeProgressAdded() {
        Skill skill = new Skill(SkillType.CREATIVITY);

        skill.addProgress(30);
        int levelsGained = skill.addProgress(-100);

        assertEquals(0, levelsGained);
        assertEquals(1, skill.getLevel());
        assertEquals(0.0, skill.getProgress());
    }

    @Test
    void addProgress_returnsZeroWhenAlreadyAtMaxLevelAndAmountPositive() {
        Skill skill = new Skill(SkillType.WRITING, 3, 3);

        int levelsGained = skill.addProgress(1000);

        assertEquals(0, levelsGained);
        assertEquals(3, skill.getLevel());
        assertEquals(0.0, skill.getProgress());
        assertTrue(skill.isMaxLevel());
    }

    @Test
    void addProgress_resetsProgressWhenReachingMaxLevel() {
        Skill skill = new Skill(SkillType.PAINTING, 1, 2);

        int levelsGained = skill.addProgress(200);

        assertEquals(1, levelsGained);
        assertEquals(2, skill.getLevel());
        assertEquals(0.0, skill.getProgress());
        assertTrue(skill.isMaxLevel());
    }

    @Test
    void toString_returnsExpectedFormat() {
        Skill skill = new Skill(SkillType.COOKING);
        skill.addProgress(25);

        assertEquals("COOKING | Level: 1 | XP: 25.0/100.0", skill.toString());
    }
}