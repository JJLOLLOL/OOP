package models.career;

import static org.junit.jupiter.api.Assertions.assertEquals;

import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class CareerListTest {

    @Test
    void softwareDeveloperFieldsAreCorrect() {
        CareerList career = CareerList.SOFTWARE_DEVELOPER;

        assertEquals("Software Developer", career.getTitle());
        assertEquals(225.0, career.getBaseSalary());
        assertEquals(8.0, career.getWorkingHours());
        assertEquals(3, career.getRelatedSkills().length);
        assertEquals(SkillType.PROGRAMMING, career.getRelatedSkills()[0]);
        assertEquals(SkillType.LOGIC, career.getRelatedSkills()[1]);
        assertEquals(SkillType.CREATIVITY, career.getRelatedSkills()[2]);
    }

    @Test
    void joblessFieldsAreCorrect() {
        CareerList career = CareerList.JOBLESS;

        assertEquals("Jobless", career.getTitle());
        assertEquals(0.0, career.getBaseSalary());
        assertEquals(0.0, career.getWorkingHours());
        assertEquals(0, career.getRelatedSkills().length);
    }
}