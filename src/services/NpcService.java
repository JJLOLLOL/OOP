package services;

import core.GameClock;
import core.WorldRegistry;
import java.util.Map;
import java.util.TreeMap;

import models.character.NPCCharacter;
import models.location.Location;

/**
 * Updates NPC locations every tick according to their daily schedule.
 */
public class NpcService {

    private final WorldRegistry world;

    /**
     * Creates an NPC updater bound to the immutable world registry.
     *
     * @param world the world registry containing all NPCs
     */
    public NpcService(WorldRegistry world) {
        this.world = world;
    }

    /**
     * Moves every NPC to the location dictated by its schedule for the current
     * in-game time.
     *
     * @param clock the current game clock
     */
    public void updateNPCLocations(GameClock clock) {
        int currentTime = clock.getTimeAsHHMM(); // e.g. 1430 for 14:30

        for (NPCCharacter npc : world.getAllNPCs()) {
            TreeMap<Integer, Location> schedule = npc.getSchedule();
            if (schedule.isEmpty()) {
                continue;
            }

            // Find the latest scheduled entry that is <= currentTime
            Map.Entry<Integer, Location> entry = schedule.floorEntry(currentTime);
            if (entry == null) {
                // Before the first entry of the day — use the last entry from "yesterday"
                entry = schedule.lastEntry();
            }

            npc.setLocation(entry.getValue());
        }
    }
}
