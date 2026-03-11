package models.Testing;

import static org.junit.Assert.*;
import org.junit.Test;

import models.Location;
import models.House;
import models.Activity;
import models.NPCCharacter;

import java.util.ArrayList;

public class HouseTest {

  @Test
  public void testUpgradedHouseConstructor() {

    ArrayList<Activity> activities = new ArrayList<>();
    ArrayList<NPCCharacter> npcs = new ArrayList<>();

    House house = new House("Luxury Villa", activities, npcs, 500000, 2.5, 5);

    assertEquals("Luxury Villa", house.getLocationName());
    assertEquals(500000, house.getHousePrice(), 0.001);
    assertEquals(2.5, house.getHouseRate(), 0.001);
    assertEquals(5, house.getHouseTier());
    assertFalse(house.isIsOwned());
  }

  @Test
  public void testNewHouseConstructorDefaults() {

    House house = new House("Starter House", new ArrayList<>(), new ArrayList<>());

    assertEquals("Starter House", house.getLocationName());
    assertEquals(0, house.getHousePrice(), 0.001);
    assertEquals(1, house.getHouseRate(), 0.001);
    assertEquals(1, house.getHouseTier());
    assertTrue(house.isIsOwned());
  }

  @Test
  public void testSetIsOwned() {

    House house = new House("Upgrade House", new ArrayList<>(), new ArrayList<>(), 200000, 1.5, 3);

    house.setIsOwned(true);

    assertTrue(house.isIsOwned());
  }



  @Test
  public void testHouseTierGetter() {

    House house = new House("Penthouse", new ArrayList<>(), new ArrayList<>(), 1000000, 3.0, 6);

    assertEquals(6, house.getHouseTier());
  }
}