package models.DoneTest;

import org.junit.Test;
import static org.junit.Assert.*;

import models.Location;
import models.NPCCharacter;
import models.actions.Furniture;

import java.util.ArrayList;

public class LocationTest {

    @Test
    public void testConstructorInitialization() {

        ArrayList<Furniture> furnitures = new ArrayList<>();

        Location location = new Location("Park", furnitures);

        assertEquals("Park", location.getLocationName());
        assertEquals(furnitures, location.getFurnitures());
    }

    @Test
    public void testSetNPCs() {

        Location location = new Location("Gym", new ArrayList<>());

        ArrayList<NPCCharacter> npcList = new ArrayList<>();
        location.setNpcs(npcList);

        assertEquals(npcList, location.getNpcs());
    }

    @Test
    public void testAddNpcCharacter() {

        Location location = new Location("Cafe", new ArrayList<>());

        NPCCharacter npc = new NPCCharacter("John", 30, "Male", new ArrayList<>());

        location.addNpcCharacter(npc);

        assertEquals(1, location.getNpcs().size());
        assertTrue(location.getNpcs().contains(npc));
    }

    @Test
    public void testRemoveNpcCharacter() {

        Location location = new Location("Library", new ArrayList<>());

        NPCCharacter npc = new NPCCharacter("Emily", 25, "Female", new ArrayList<>());

        location.addNpcCharacter(npc);
        location.removeNpcCharacter(npc);

        assertEquals(0, location.getNpcs().size());
    }

    @Test
    public void testFurnitureListModification() {

        ArrayList<Furniture> furnitures = new ArrayList<>();

        Location location = new Location("Beach", furnitures);

        furnitures.add(null);

        assertEquals(1, location.getFurnitures().size());
    }
}