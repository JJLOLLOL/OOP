package models.career;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Types.CareerList;
import Types.CareerRankList;
import org.junit.jupiter.api.Test;

class CareerTest {

    @Test
    void constructorUsesCareerDefaults() {
        Career career = new Career(CareerList.SOFTWARE_DEVELOPER);

        assertEquals(CareerList.SOFTWARE_DEVELOPER, career.getCurrentCareer());
        assertEquals("Software Developer", career.getTitle());
        assertEquals(8.0, career.getWorkingHours());
        assertEquals("Intern", career.getRank());
        assertEquals(1, career.getCurrentRank());
        assertEquals(112.5, career.getSalary());
        assertEquals(0.0, career.getProgress());
        assertEquals(100.0, career.getRequiredXP());
    }

    @Test
    void addProgressWithoutPromotionReturnsProgressMessage() {
        Career career = new Career(CareerList.CHEF);

        String result = career.addProgress(30.0);

        assertTrue(result.contains("Progress: 30.0 / 100.0 XP"));
        assertEquals(1, career.getCurrentRank());
        assertEquals(30.0, career.getProgress());
    }

    @Test
    void addProgressPromotesAndUpdatesRequiredXp() {
        Career career = new Career(CareerList.TEACHER);

        String result = career.addProgress(100.0);

        assertTrue(result.contains("Promoted to Junior Employee"));
        assertTrue(result.contains("150.0 XP"));
        assertEquals(2, career.getCurrentRank());
        assertEquals("Junior Employee", career.getRank());
        assertEquals(0.0, career.getProgress());
        assertEquals(150.0, career.getRequiredXP());
    }

    @Test
    void addProgressAtMaxRankReturnsMaxRankMessage() {
        Career career = new Career(CareerList.DOCTOR);

        for (int i = 1; i < CareerRankList.count(); i++) {
            career.addProgress(10_000.0);
        }

        String result = career.addProgress(10.0);

        assertTrue(result.contains("Max rank attained"));
        assertEquals(CareerRankList.count(), career.getCurrentRank());
        assertEquals("Executive", career.getRank());
    }
}
