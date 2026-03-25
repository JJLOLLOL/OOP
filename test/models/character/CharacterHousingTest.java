package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import models.actions.Furniture;
import models.character.finances.CharacterFinances;
import models.character.housing.CharacterHousing;
import models.location.House;
import org.junit.jupiter.api.Test;

class CharacterHousingTest {

    @Test
    void assignHouseRequiresNonNullHouse() {
        CharacterHousing housing = new CharacterHousing();

        assertFalse(housing.hasHouse());
        assertThrows(IllegalArgumentException.class, () -> housing.assignHouse(null));
    }

    @Test
    void purchaseFurnitureValidatesInputsAndRequiresCurrentHouse() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        Furniture chair = new Furniture("Chair", "Desk chair", 100.0);

        assertThrows(IllegalArgumentException.class, () -> housing.purchaseFurniture(null, finances));
        assertThrows(IllegalArgumentException.class, () -> housing.purchaseFurniture(chair, null));
        assertThrows(IllegalArgumentException.class, () -> housing.purchaseFurniture(chair, finances));
    }

    @Test
    void purchaseFurnitureAddsFurnitureAndDeductsMoney() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House house = new House("Starter", new ArrayList<>());
        Furniture chair = new Furniture("Chair", "Desk chair", 100.0);
        housing.assignHouse(house);

        housing.purchaseFurniture(chair, finances);

        assertEquals(1, house.getFurnitureCount());
        assertEquals(900.0, finances.getMoney());
    }

    @Test
    void purchaseHouseRequiresExistingHouseAndEnoughFunds() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House starter = new House("Starter", new ArrayList<>());
        House villa = new House("Villa", new ArrayList<>(), 1500.0, 2.0, 3);

        assertThrows(IllegalStateException.class, () -> housing.purchaseHouse(villa, finances));

        housing.assignHouse(starter);
        assertThrows(IllegalStateException.class, () -> housing.purchaseHouse(villa, finances));
    }

    @Test
    void purchaseHouseUpgradesCurrentHouseAndDeductsMoney() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House starter = new House("Starter", new ArrayList<>());
        House upgrade = new House("Upgrade", new ArrayList<>(), 500.0, 1.5, 2);
        housing.assignHouse(starter);

        housing.purchaseHouse(upgrade, finances);

        assertTrue(housing.hasHouse());
        assertEquals(2, starter.getTier());
        assertEquals(500.0, finances.getMoney());
    }
}
