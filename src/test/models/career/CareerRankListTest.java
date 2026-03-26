package models.career;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CareerRankListTest {

    @Test
    void getTitle_returnsCorrectTitles() {
        assertEquals("Intern", CareerRankList.INTERN.getTitle());
        assertEquals("Junior Employee", CareerRankList.JUNIOR_EMPLOYEE.getTitle());
        assertEquals("Employee", CareerRankList.EMPLOYEE.getTitle());
        assertEquals("Senior Employee", CareerRankList.SENIOR_EMPLOYEE.getTitle());
        assertEquals("Manager", CareerRankList.MANAGER.getTitle());
        assertEquals("Director", CareerRankList.DIRECTOR.getTitle());
        assertEquals("Executive", CareerRankList.EXECUTIVE.getTitle());
    }

    @Test
    void getSalaryMultiplier_returnsCorrectValues() {
        assertEquals(0.5, CareerRankList.INTERN.getSalaryMultiplier());
        assertEquals(1.0, CareerRankList.JUNIOR_EMPLOYEE.getSalaryMultiplier());
        assertEquals(1.25, CareerRankList.EMPLOYEE.getSalaryMultiplier());
        assertEquals(1.7, CareerRankList.SENIOR_EMPLOYEE.getSalaryMultiplier());
        assertEquals(2.2, CareerRankList.MANAGER.getSalaryMultiplier());
        assertEquals(2.8, CareerRankList.DIRECTOR.getSalaryMultiplier());
        assertEquals(3.5, CareerRankList.EXECUTIVE.getSalaryMultiplier());
    }

    @Test
    void fromRank_returnsCorrectEnumForValidRanks() {
        assertEquals(CareerRankList.INTERN, CareerRankList.fromRank(1));
        assertEquals(CareerRankList.JUNIOR_EMPLOYEE, CareerRankList.fromRank(2));
        assertEquals(CareerRankList.EMPLOYEE, CareerRankList.fromRank(3));
        assertEquals(CareerRankList.SENIOR_EMPLOYEE, CareerRankList.fromRank(4));
        assertEquals(CareerRankList.MANAGER, CareerRankList.fromRank(5));
        assertEquals(CareerRankList.DIRECTOR, CareerRankList.fromRank(6));
        assertEquals(CareerRankList.EXECUTIVE, CareerRankList.fromRank(7));
    }

    @Test
    void fromRank_throwsExceptionForInvalidLowRank() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> CareerRankList.fromRank(0));
        assertEquals("Invalid rank: 0", ex.getMessage());
    }

    @Test
    void fromRank_throwsExceptionForInvalidHighRank() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> CareerRankList.fromRank(8));
        assertEquals("Invalid rank: 8", ex.getMessage());
    }

    @Test
    void count_returnsNumberOfRanks() {
        assertEquals(7, CareerRankList.count());
        assertEquals(CareerRankList.values().length, CareerRankList.count());
    }
}