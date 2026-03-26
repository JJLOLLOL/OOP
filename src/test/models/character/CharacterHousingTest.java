package models.character;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import models.character.finances.CharacterFinances;
import models.character.housing.CharacterHousing;
import models.furniture.Furniture;
import models.location.House;

class CharacterHousingTest {

    private House makeHouse(String name, double price, int tier) {
        return new House(name, new ArrayList<>(), price, 1.0, tier);
    }

    private Furniture makeFurniture(String name, double price) {
        return new Furniture(name, name + " desc", price);
    }

    @Test
    void hasHouse_returnsFalseInitially() {
        CharacterHousing housing = new CharacterHousing();

        assertFalse(housing.hasHouse());
        assertNull(housing.getCurrentHouse());
    }

    @Test
    void assignHouse_setsCurrentHouse() {
        CharacterHousing housing = new CharacterHousing();
        House house = makeHouse("Starter House", 0.0, 1);

        housing.assignHouse(house);

        assertTrue(housing.hasHouse());
        assertSame(house, housing.getCurrentHouse());
    }

    @Test
    void assignHouse_throwsExceptionWhenHouseIsNull() {
        CharacterHousing housing = new CharacterHousing();

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> housing.assignHouse(null));

        assertEquals("House cannot be null.", ex.getMessage());
    }

    @Test
    void buyFurniture_returnsSuccess_whenHouseHasSpaceAndFinancesCanAfford() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House house = makeHouse("Starter House", 0.0, 1);
        Furniture chair = makeFurniture("Chair", 200.0);

        housing.assignHouse(house);
        CharacterHousing.HousingResult result = housing.buyFurniture(chair, finances);

        assertEquals(CharacterHousing.HousingResult.SUCCESS, result);
        assertTrue(house.containsFurniture(chair));
        assertEquals(800.0, finances.getMoney());
    }

    @Test
    void buyFurniture_returnsHouseFull_whenHouseCapacityReached() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House house = makeHouse("Starter House", 0.0, 1);

        housing.assignHouse(house);
        for (int i = 1; i <= house.getMaxFurnitureCapacity(); i++) {
            house.addFurniture(makeFurniture("Furniture " + i, 10.0));
        }

        CharacterHousing.HousingResult result =
                housing.buyFurniture(makeFurniture("Extra", 100.0), finances);

        assertEquals(CharacterHousing.HousingResult.HOUSE_FULL, result);
        assertEquals(1000.0, finances.getMoney());
    }

    @Test
    void buyFurniture_returnsInsufficientFunds_whenFinancesCannotAfford() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House house = makeHouse("Starter House", 0.0, 1);
        Furniture luxuryBed = makeFurniture("Luxury Bed", 1200.0);

        housing.assignHouse(house);
        CharacterHousing.HousingResult result = housing.buyFurniture(luxuryBed, finances);

        assertEquals(CharacterHousing.HousingResult.INSUFFICIENT_FUNDS, result);
        assertFalse(house.containsFurniture(luxuryBed));
        assertEquals(1000.0, finances.getMoney());
    }

    @Test
    void buyFurniture_throwsExceptionWhenFurnitureIsNull() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        housing.assignHouse(makeHouse("Starter House", 0.0, 1));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> housing.buyFurniture(null, finances));

        assertEquals("Furniture cannot be null.", ex.getMessage());
    }

    @Test
    void buyFurniture_throwsExceptionWhenFinancesIsNull() {
        CharacterHousing housing = new CharacterHousing();
        housing.assignHouse(makeHouse("Starter House", 0.0, 1));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> housing.buyFurniture(makeFurniture("Chair", 100.0), null));

        assertEquals("Finances cannot be null.", ex.getMessage());
    }

    @Test
    void buyFurniture_throwsExceptionWhenCurrentHouseIsNull() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> housing.buyFurniture(makeFurniture("Chair", 100.0), finances));

        assertEquals("House cannot be null.", ex.getMessage());
    }

    @Test
    void sellFurniture_returnsSuccess_whenFurnitureExistsInHouse() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House house = makeHouse("Starter House", 0.0, 1);
        Furniture sofa = makeFurniture("Sofa", 400.0);

        housing.assignHouse(house);
        house.addFurniture(sofa);

        CharacterHousing.HousingResult result = housing.sellFurniture(sofa, finances);

        assertEquals(CharacterHousing.HousingResult.SUCCESS, result);
        assertFalse(house.containsFurniture(sofa));
        assertEquals(1200.0, finances.getMoney());
    }

    @Test
    void sellFurniture_returnsHouseEmpty_whenFurnitureDoesNotExistInHouse() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House house = makeHouse("Starter House", 0.0, 1);
        Furniture sofa = makeFurniture("Sofa", 400.0);

        housing.assignHouse(house);
        CharacterHousing.HousingResult result = housing.sellFurniture(sofa, finances);

        assertEquals(CharacterHousing.HousingResult.HOUSE_EMPTY, result);
        assertEquals(1000.0, finances.getMoney());
    }

    @Test
    void sellFurniture_throwsExceptionWhenFurnitureIsNull() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        housing.assignHouse(makeHouse("Starter House", 0.0, 1));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> housing.sellFurniture(null, finances));

        assertEquals("furniture cannot be null.", ex.getMessage());
    }

    @Test
    void buyHouse_returnsSuccess_whenFinancesCanAfford() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House currentHouse = makeHouse("Starter House", 0.0, 1);
        House targetHouse = makeHouse("Luxury House", 600.0, 4);

        housing.assignHouse(currentHouse);
        CharacterHousing.HousingResult result = housing.buyHouse(targetHouse, finances);

        assertEquals(CharacterHousing.HousingResult.SUCCESS, result);
        assertEquals(4, currentHouse.getTier());
        assertEquals(600.0, currentHouse.getPrice());
        assertEquals(400.0, finances.getMoney());
    }

    @Test
    void buyHouse_returnsInsufficientFunds_whenFinancesCannotAfford() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        House currentHouse = makeHouse("Starter House", 0.0, 1);
        House mansion = makeHouse("Mansion", 1500.0, 5);

        housing.assignHouse(currentHouse);
        CharacterHousing.HousingResult result = housing.buyHouse(mansion, finances);

        assertEquals(CharacterHousing.HousingResult.INSUFFICIENT_FUNDS, result);
        assertEquals(1, currentHouse.getTier());
        assertEquals(0.0, currentHouse.getPrice());
        assertEquals(1000.0, finances.getMoney());
    }

    @Test
    void buyHouse_throwsExceptionWhenTargetHouseIsNull() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();
        housing.assignHouse(makeHouse("Starter House", 0.0, 1));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> housing.buyHouse(null, finances));

        assertEquals("House cannot be null.", ex.getMessage());
    }

    @Test
    void buyHouse_throwsExceptionWhenFinancesIsNull() {
        CharacterHousing housing = new CharacterHousing();
        housing.assignHouse(makeHouse("Starter House", 0.0, 1));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> housing.buyHouse(makeHouse("Upgrade", 500.0, 3), null));

        assertEquals("Finances cannot be null.", ex.getMessage());
    }

    @Test
    void buyHouse_throwsExceptionWhenCurrentHouseIsNull() {
        CharacterHousing housing = new CharacterHousing();
        CharacterFinances finances = new CharacterFinances();

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> housing.buyHouse(makeHouse("Upgrade", 500.0, 3), finances));

        assertEquals("House cannot be null.", ex.getMessage());
    }

    @Test
    void housingResult_values_containsAllExpectedConstants() {
        CharacterHousing.HousingResult[] results = CharacterHousing.HousingResult.values();

        assertEquals(4, results.length);
        assertEquals(CharacterHousing.HousingResult.SUCCESS, results[0]);
        assertEquals(CharacterHousing.HousingResult.HOUSE_FULL, results[1]);
        assertEquals(CharacterHousing.HousingResult.HOUSE_EMPTY, results[2]);
        assertEquals(CharacterHousing.HousingResult.INSUFFICIENT_FUNDS, results[3]);
    }
}