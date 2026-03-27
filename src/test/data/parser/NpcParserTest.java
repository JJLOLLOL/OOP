package data.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.character.NPCCharacter;
import models.location.Location;
import types.Gender;

class NpcParserTest {

    @Test
    void parse_shouldBuildNpcsAndIgnoreUnknownScheduleLocations() throws IOException {
        Location starterHome = new Location("Starter Home", new ArrayList<>());
        Location lounge = new Location("Lounge", new ArrayList<>());
        Map<String, Location> locations = new HashMap<>();
        locations.put(starterHome.getLocationName(), starterHome);
        locations.put(lounge.getLocationName(), lounge);

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        List<NPCCharacter> npcs;
        try {
            System.setErr(new PrintStream(errContent));
            npcs = new NpcParser().parse("test/fixtures/data/npcs-fixture.txt", locations);
        } finally {
            System.setErr(originalErr);
        }

        NPCCharacter npc = npcs.get(0);
        assertEquals(1, npcs.size());
        assertEquals("Alex", npc.getName());
        assertEquals(29, npc.getAge());
        assertEquals(Gender.FEMALE, npc.getGender());
        assertEquals("Test NPC.", npc.getDescription());
        assertSame(starterHome, npc.getLocation());
        assertEquals(2, npc.getSchedule().size());
        assertSame(lounge, npc.getSchedule().get(1800));
        assertTrue(errContent.toString().contains("unknown location: UnknownPlace"));
    }

    @Test
    void parse_shouldThrow_whenNpcHasNoValidScheduleEntries() {
        Map<String, Location> locations = new HashMap<>();
        locations.put("Starter Home", new Location("Starter Home", new ArrayList<>()));

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        try {
            System.setErr(new PrintStream(errContent));
            assertThrows(
                    IllegalStateException.class,
                    () -> new NpcParser().parse("test/fixtures/data/npcs-invalid-fixture.txt", locations));
        } finally {
            System.setErr(originalErr);
        }

        assertTrue(errContent.toString().contains("NPC 'Broken NPC' has a schedule with an unknown location"));
    }
}
