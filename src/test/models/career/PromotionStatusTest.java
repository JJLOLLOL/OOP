package models.career;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PromotionStatusTest {

    @Test
    void values_containsAllStatusesInOrder() {
        PromotionStatus[] statuses = PromotionStatus.values();

        assertEquals(3, statuses.length);
        assertEquals(PromotionStatus.NONE, statuses[0]);
        assertEquals(PromotionStatus.PROMOTED, statuses[1]);
        assertEquals(PromotionStatus.MAX_RANK, statuses[2]);
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(PromotionStatus.NONE, PromotionStatus.valueOf("NONE"));
        assertEquals(PromotionStatus.PROMOTED, PromotionStatus.valueOf("PROMOTED"));
        assertEquals(PromotionStatus.MAX_RANK, PromotionStatus.valueOf("MAX_RANK"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> PromotionStatus.valueOf("INVALID"));
    }
}