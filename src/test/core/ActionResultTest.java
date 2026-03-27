package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActionResultTest {

    @Test
    void successFactoryCreatesSuccessfulResult() {
        ActionResult result = ActionResult.success("Worked shift");

        assertTrue(result.isSuccess());
        assertEquals("Worked shift", result.getMessage());
    }

    @Test
    void failureFactoryCreatesFailedResult() {
        ActionResult result = ActionResult.failure("Not enough money");

        assertFalse(result.isSuccess());
        assertEquals("Not enough money", result.getMessage());
    }
}
