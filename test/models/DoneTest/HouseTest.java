package models.DoneTest;

import static org.junit.Assert.*;
import org.junit.Test;

import models.NPCCharacter;
import models.actions.Furniture;
import models.location.House;

import java.util.ArrayList;

public class HouseTest {

    @Test
    public void testUpgradedHouseConstructor() {

        ArrayList<Furniture> furnitures = new ArrayList<>();

        House house = new House("Luxury Villa", furnitures, 500000, 2.5, 5);

        assertEquals("Luxury Villa", house.getLocationName());
        assertEquals(500000, house.getHousePrice(), 0.001);
        assertEquals(2.5, house.getHouseRate(), 0.001);
        assertEquals(5, house.getHouseTier());
        assertFalse(house.isOwned());
    }

    @Test
    public void testStarterHouseConstructorDefaults() {

        House house = new House("Starter House", new ArrayList<>());

        assertEquals("Starter House", house.getLocationName());
        assertEquals(0, house.getHousePrice(), 0.001);
        assertEquals(1, house.getHouseRate(), 0.001);
        assertEquals(1, house.getHouseTier());
        assertTrue(house.isOwned());
    }

    @Test
    public void testUpgradeHouse() {

        House tier1 = new House("Starter House", new ArrayList<>());
        House tier2 = new House("Family House", new ArrayList<>(), 200000, 1.5, 2);

        tier1.upgradeHouse(tier2);

        assertFalse(tier1.isOwned());
        assertTrue(tier2.isOwned());
    }

    @Test
    public void testHouseTierGetter() {

        House house = new House("Penthouse", new ArrayList<>(), 1000000, 3.0, 6);

        assertEquals(6, house.getHouseTier());
    }
}