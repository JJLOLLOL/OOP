package models.Testing;

import org.junit.Test;
import static org.junit.Assert.*;

import models.Location;
import models.Activity;
import models.NPCCharacter;

import java.util.ArrayList;

public class LocationTest {

  @Test
  public void testConstructorInitialization() {

    ArrayList<Activity> activities = new ArrayList<>();
    ArrayList<NPCCharacter> npcs = new ArrayList<>();

    Location location = new Location("Park", activities, npcs);
    System.out.println(location.getLocationName());
    assertEquals("Park", location.getLocationName());
    assertEquals(activities, location.getActivities());
    assertEquals(npcs, location.getNpcs());
  }


  @Test
  public void testSetActivities() {

    Location location = new Location("Mall", new ArrayList<>(), new ArrayList<>());

    ArrayList<Activity> newActivities = new ArrayList<>();
    location.setActivities(newActivities);

    assertEquals(newActivities, location.getActivities());
  }

  @Test
  public void testSetNPCs() {

    Location location = new Location("Gym", new ArrayList<>(), new ArrayList<>());

    ArrayList<NPCCharacter> npcList = new ArrayList<>();
    location.setNpcs(npcList);

    assertEquals(npcList, location.getNpcs());
  }

  @Test
  public void testActivitiesListModification() {

    ArrayList<Activity> activities = new ArrayList<>();
    Location location = new Location("Beach", activities, new ArrayList<>());

    activities.add(null); 

    assertEquals(1, location.getActivities().size());
  }

}