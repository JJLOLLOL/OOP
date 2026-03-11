package models.DoneTest;

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

    Location location = new Location("Park", activities);
    System.out.println(location.getLocationName());
    assertEquals("Park", location.getLocationName());
    assertEquals(activities, location.getActivities());
  }


  @Test
  public void testSetActivities() {

    Location location = new Location("Mall", new ArrayList<>());

    ArrayList<Activity> newActivities = new ArrayList<>();
    location.setActivities(newActivities);

    assertEquals(newActivities, location.getActivities());
  }

  @Test
  public void testSetNPCs() {

    Location location = new Location("Gym", new ArrayList<>());

    ArrayList<NPCCharacter> npcList = new ArrayList<>();
    location.setNpcs(npcList);

    assertEquals(npcList, location.getNpcs());
  }

  @Test
  public void testActivitiesListModification() {

    ArrayList<Activity> activities = new ArrayList<>();
    Location location = new Location("Beach", activities);

    activities.add(null); 

    assertEquals(1, location.getActivities().size());
  }

}