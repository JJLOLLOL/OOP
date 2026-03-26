package types;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import testTypes.AchievementList;


class AchievementListTest {

    @Test
    void values_containsAllAchievements() {
        AchievementList[] achievements = AchievementList.values();

        assertEquals(21, achievements.length);
        assertEquals(AchievementList.FIRST_JOB, achievements[0]);
        assertEquals(AchievementList.EVIL, achievements[20]);
    }

    @Test
    void getTitle_returnsCorrectValues() {
        assertEquals("First Job", AchievementList.FIRST_JOB.getTitle());
        assertEquals("Tech Trailblazer", AchievementList.TECH_TRAILBLAZER.getTitle());
        assertEquals("First Cooking", AchievementList.FIRST_COOKING.getTitle());
        assertEquals("Friendly", AchievementList.FRIENDLY.getTitle());
    }

    @Test
    void getDescription_returnsCorrectValues() {
        assertEquals("Get your first non-jobless career",
                AchievementList.FIRST_JOB.getDescription());
        assertEquals("Reach career rank 7",
                AchievementList.CORPORATE_EXECUTIVE.getDescription());
        assertEquals("Use Painting skill for the first time",
                AchievementList.FIRST_PAINTING.getDescription());
        assertEquals("Become enemies with every other character",
                AchievementList.EVIL.getDescription());
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(AchievementList.FIRST_JOB, AchievementList.valueOf("FIRST_JOB"));
        assertEquals(AchievementList.FIRST_PROMOTION, AchievementList.valueOf("FIRST_PROMOTION"));
        assertEquals(AchievementList.FIRST_PROGRAMMING,
                AchievementList.valueOf("FIRST_PROGRAMMING"));
        assertEquals(AchievementList.FRIENDLY, AchievementList.valueOf("FRIENDLY"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> AchievementList.valueOf("INVALID"));
    }
}