import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import Types.CareerRankList;
import org.junit.jupiter.api.Test;

class CareerRankListTest {

    @Test
    void fromRankReturnsExpectedBounds() {
        assertEquals(CareerRankList.INTERN, CareerRankList.fromRank(1));
        assertEquals(CareerRankList.EXECUTIVE, CareerRankList.fromRank(CareerRankList.count()));
    }

    @Test
    void fromRankRejectsOutOfRangeValues() {
        assertThrows(IllegalArgumentException.class, () -> CareerRankList.fromRank(0));
        assertThrows(IllegalArgumentException.class, () -> CareerRankList.fromRank(CareerRankList.count() + 1));
    }
}
