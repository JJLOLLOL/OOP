package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import models.character.SimCharacter;
import models.location.House;
import types.Gender;

class HouseServiceTest {

    @Test
    void purchaseHouseUpgradesCurrentHouseAndDeductsMoney() {
        House current = new House("Home", new ArrayList<>(), 0.0, 1.0, 1);
        House target = new House("Villa", new ArrayList<>(), 500.0, 2.0, 3);
        SimCharacter buyer = new SimCharacter("Alex", 25, Gender.MALE, current);
        buyer.assignHouse(current);

        boolean success = HouseService.purchaseHouse(buyer, target);

        assertTrue(success);
        assertEquals(500.0, buyer.getMoney());
        assertEquals(3, buyer.getCurrentHouse().getTier());
        assertEquals(500.0, buyer.getCurrentHouse().getPrice());
    }

    @Test
    void purchaseHouseFailsWhenFundsAreInsufficient() {
        House current = new House("Home", new ArrayList<>(), 0.0, 1.0, 1);
        House target = new House("Villa", new ArrayList<>(), 1500.0, 2.0, 3);
        SimCharacter buyer = new SimCharacter("Alex", 25, Gender.MALE, current);
        buyer.assignHouse(current);

        assertFalse(HouseService.purchaseHouse(buyer, target));
        assertEquals(1000.0, buyer.getMoney());
        assertEquals(1, buyer.getCurrentHouse().getTier());
    }

    @Test
    void upgradeHouseRequiresNextTierAndEnoughMoney() {
        House current = new House("Home", new ArrayList<>(), 200.0, 1.0, 1);
        House next = new House("Townhouse", new ArrayList<>(), 500.0, 1.5, 2);
        House tooHigh = new House("Villa", new ArrayList<>(), 900.0, 2.0, 4);
        SimCharacter owner = new SimCharacter("Alex", 25, Gender.MALE, current);
        owner.assignHouse(current);

        assertFalse(HouseService.upgradeHouse(owner, tooHigh));
        assertTrue(HouseService.upgradeHouse(owner, next));
        assertSame(current, owner.getLocation());
        assertEquals(700.0, owner.getMoney());
        assertEquals(2, owner.getCurrentHouse().getTier());
    }

    @Test
    void getPurchaseMessageReturnsFormattedPurchaseString() {
        House house = new House("Villa", new ArrayList<>(), 500.0, 2.0, 3);
        SimCharacter buyer = new SimCharacter("Alex", 25, Gender.MALE, house);

        assertEquals("Alex purchased Villa for $500.0!",
                HouseService.getPurchaseMessage(buyer, house, false));
    }
}
