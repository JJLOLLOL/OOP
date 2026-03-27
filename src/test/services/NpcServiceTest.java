package services;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import core.GameClock;
import core.WorldRegistry;
import models.character.NPCCharacter;
import models.location.House;
import models.location.Location;
import types.Gender;

class NpcServiceTest {

    @Test
    void updateNpcLocationsUsesLastEntryBeforeFirstScheduleAndFloorEntryAfterwards() {
        House home = new House("Home", new ArrayList<>());
        Location park = new Location("Park", new ArrayList<>());
        TreeMap<Integer, Location> schedule = new TreeMap<>();
        schedule.put(900, park);
        schedule.put(2300, home);
        NPCCharacter npc = new NPCCharacter("Taylor", 30, Gender.FEMALE, "Walker", schedule);

        Map<String, Location> locations = new LinkedHashMap<>();
        locations.put("Home", home);
        locations.put("Park", park);
        WorldRegistry world = new WorldRegistry(locations, List.of(npc));
        NpcService service = new NpcService(world);
        GameClock clock = new GameClock();

        service.updateNPCLocations(clock);
        assertSame(home, npc.getLocation());

        clock.advanceHours(2.0);
        service.updateNPCLocations(clock);
        assertSame(park, npc.getLocation());
    }
}
