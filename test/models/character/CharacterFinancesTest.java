package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import models.character.finances.CharacterFinances;
import org.junit.jupiter.api.Test;

class CharacterFinancesTest {

    @Test
    void constructorStartsWithDefaultMoney() {
        CharacterFinances finances = new CharacterFinances();

        assertEquals(1000.0, finances.getMoney());
        assertTrue(finances.canAfford(1000.0));
    }

    @Test
    void spendMoneyRejectsNegativeAndFailsWhenInsufficient() {
        CharacterFinances finances = new CharacterFinances();

        assertThrows(IllegalArgumentException.class, () -> finances.spendMoney(-1.0));
        assertFalse(finances.spendMoney(2000.0));
        assertEquals(1000.0, finances.getMoney());
    }

    @Test
    void earnAndSpendMoneyUpdateBalance() {
        CharacterFinances finances = new CharacterFinances();

        finances.earnMoney(250.0);
        boolean spent = finances.spendMoney(400.0);

        assertTrue(spent);
        assertEquals(850.0, finances.getMoney());
    }

    @Test
    void earnMoneyRejectsNegative() {
        CharacterFinances finances = new CharacterFinances();

        assertThrows(IllegalArgumentException.class, () -> finances.earnMoney(-5.0));
    }
}
