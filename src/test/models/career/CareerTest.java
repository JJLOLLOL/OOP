package models.career;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class CareerTest {

    @Test
    void constructorRejectsNullCareer() {
        assertThrows(IllegalArgumentException.class, () -> new Career(null));
    }

    @Test
    void constructorInitialisesDefaultState() {
        Career career = new Career(CareerList.SOFTWARE_DEVELOPER);

        assertEquals(CareerList.SOFTWARE_DEVELOPER, career.getCurrentCareer());
        assertEquals("Software Developer", career.getTitle());
        assertEquals(1, career.getCurrentRank());
        assertEquals("Intern", career.getRank());
        assertEquals(100.0, career.getRequiredXP());
        assertEquals(0.0, career.getProgress());
    }

    @Test
    void joblessHelpersWorkCorrectly() {
        Career career = new Career(CareerList.JOBLESS);

        assertEquals(true, career.isJobless());
        assertEquals(0.0, career.getWorkingHours());
        assertEquals(9, career.getShiftStartHour());
        assertEquals(9, career.getShiftEndHour());
    }

    @Test
    void shiftHelpersUseConfiguredWorkingHours() {
        Career career = new Career(CareerList.ENGINEER);

        assertEquals(8.0, career.getWorkingHours());
        assertEquals(9, career.getShiftStartHour());
        assertEquals(17, career.getShiftEndHour());
        assertEquals(false, career.hasShiftStarted(8.5));
        assertEquals(true, career.hasShiftStarted(9.0));
        assertEquals(false, career.isShiftOver(16.5));
        assertEquals(true, career.isShiftOver(17.0));
        assertEquals(0.0, career.getRemainingShiftHours(8.0));
        assertEquals(4.5, career.getRemainingShiftHours(12.5));
        assertEquals(0.0, career.getRemainingShiftHours(17.0));
    }

    @Test
    void workFractionAndPayAreCalculatedFromSalary() {
        Career career = new Career(CareerList.SOFTWARE_DEVELOPER);

        assertEquals(0.5, career.getWorkFraction(4.0));
        assertEquals(56.25, career.calculatePay(4.0));
        assertEquals(0.0, new Career(CareerList.JOBLESS).getWorkFraction(5.0));
    }

    @Test
    void salaryUsesRankMultiplier() {
        Career career = new Career(CareerList.DOCTOR);

        assertEquals(150.0, career.getSalary());
        career.addProgress(100.0);
        assertEquals(300.0, career.getSalary());
    }

    @Test
    void relatedSkillsComeFromCareerList() {
        Career career = new Career(CareerList.CHEF);

        assertEquals(2, career.getRelatedSkills().length);
        assertEquals(SkillType.COOKING, career.getRelatedSkills()[0]);
        assertEquals(SkillType.CREATIVITY, career.getRelatedSkills()[1]);
    }

    @Test
    void addProgressReturnsNoneForNonPositiveAmount() {
        Career career = new Career(CareerList.TEACHER);

        assertEquals(PromotionStatus.NONE, career.addProgress(0.0));
        assertEquals(PromotionStatus.NONE, career.addProgress(-5.0));
        assertEquals(0.0, career.getProgress());
        assertEquals(1, career.getCurrentRank());
    }

    @Test
    void addProgressPromotesWhenThresholdReached() {
        Career career = new Career(CareerList.ACCOUNTANT);

        PromotionStatus status = career.addProgress(100.0);

        assertEquals(PromotionStatus.PROMOTED, status);
        assertEquals(2, career.getCurrentRank());
        assertEquals("Junior Employee", career.getRank());
        assertEquals(0.0, career.getProgress());
        assertEquals(150.0, career.getRequiredXP());
    }

    @Test
    void addProgressCarriesExcessXpAfterPromotion() {
        Career career = new Career(CareerList.ENGINEER);

        PromotionStatus status = career.addProgress(130.0);

        assertEquals(PromotionStatus.PROMOTED, status);
        assertEquals(2, career.getCurrentRank());
        assertEquals(30.0, career.getProgress());
        assertEquals(150.0, career.getRequiredXP());
    }

    @Test
    void addProgressReturnsMaxRankWhenAlreadyAtTop() {
        Career career = new Career(CareerList.LAWYER);

        career.addProgress(100.0);
        career.addProgress(150.0);
        career.addProgress(225.0);
        career.addProgress(337.5);
        career.addProgress(506.25);
        career.addProgress(759.375);

        assertEquals(7, career.getCurrentRank());
        assertEquals("Executive", career.getRank());
        assertEquals(PromotionStatus.MAX_RANK, career.addProgress(10.0));
        assertEquals(7, career.getCurrentRank());
    }
}