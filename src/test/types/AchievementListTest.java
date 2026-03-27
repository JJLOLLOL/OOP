package types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class AchievementListTest {

    @Test
    void values_containsAllAchievements() {
        AchievementType[] achievements = AchievementType.values();

        assertEquals(21, achievements.length);
        assertEquals(AchievementType.FIRST_JOB, achievements[0]);
        assertEquals(AchievementType.EVIL, achievements[20]);
    }

    @Test
    void getTitle_returnsCorrectValues() {
        assertEquals("First Job", AchievementType.FIRST_JOB.getTitle());
        assertEquals("Tech Trailblazer", AchievementType.TECH_TRAILBLAZER.getTitle());
        assertEquals("First Cooking", AchievementType.FIRST_COOKING.getTitle());
        assertEquals("Friendly", AchievementType.FRIENDLY.getTitle());
    }

    @Test
    void getDescription_returnsCorrectValues() {
        assertEquals("Get your first non-jobless career",
                AchievementType.FIRST_JOB.getDescription());
        assertEquals("Reach career rank 7",
                AchievementType.CORPORATE_EXECUTIVE.getDescription());
        assertEquals("Use Painting skill for the first time",
                AchievementType.FIRST_PAINTING.getDescription());
        assertEquals("Become enemies with every other character",
                AchievementType.EVIL.getDescription());
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(AchievementType.FIRST_JOB, AchievementType.valueOf("FIRST_JOB"));
        assertEquals(AchievementType.FIRST_PROMOTION, AchievementType.valueOf("FIRST_PROMOTION"));
        assertEquals(AchievementType.FIRST_PROGRAMMING,
                AchievementType.valueOf("FIRST_PROGRAMMING"));
        assertEquals(AchievementType.FRIENDLY, AchievementType.valueOf("FRIENDLY"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> AchievementType.valueOf("INVALID"));
    }
}