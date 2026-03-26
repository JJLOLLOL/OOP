package models.action;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ActionTypeTest {

    @Test
    void values_containsAllActionTypesInOrder() {
        ActionType[] actionTypes = ActionType.values();

        assertEquals(3, actionTypes.length);
        assertEquals(ActionType.SOCIALISE, actionTypes[0]);
        assertEquals(ActionType.EAT, actionTypes[1]);
        assertEquals(ActionType.SLEEP, actionTypes[2]);
    }

    @Test
    void valueOf_returnsCorrectEnum() {
        assertEquals(ActionType.SOCIALISE, ActionType.valueOf("SOCIALISE"));
        assertEquals(ActionType.EAT, ActionType.valueOf("EAT"));
        assertEquals(ActionType.SLEEP, ActionType.valueOf("SLEEP"));
    }

    @Test
    void valueOf_throwsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> ActionType.valueOf("INVALID"));
    }
}