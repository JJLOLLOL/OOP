package models.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.TreeMap;
import models.location.Location;
import org.junit.jupiter.api.Test;

class NPCCharacterTest {

    @Test
    void constructorUsesFirstScheduledLocationAndStoresMetadata() {
        Location home = new Location("Home", new ArrayList<>());
        Location cafe = new Location("Cafe", new ArrayList<>());
        TreeMap<Integer, Location> schedule = new TreeMap<>();
        schedule.put(1200, cafe);
        schedule.put(900, home);

        NPCCharacter npc = new NPCCharacter("Sam", 28, "F", "Barista", schedule);

        assertEquals("Sam", npc.getName());
        assertEquals(28, npc.getAge());
        assertEquals("F", npc.getGender());
        assertEquals("Barista", npc.getDescription());
        assertSame(home, npc.getLocation());
        assertSame(schedule, npc.getSchedule());
        assertSame(home, npc.getSchedule().firstEntry().getValue());
    }
}
