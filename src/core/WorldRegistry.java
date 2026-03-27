package core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import models.character.*;
import models.location.Location;

/**
 * Holds all static world data — locations and NPCs — built once at startup.
 *
 * <p>
 * Previously split across {@code WorldRegistry} (storage) and
 * {@code WorldBuilder} (construction). The construction logic has been moved
 * to the {@code data} package to decouple the core from specific game data.
 *
 * <p>
 * All collections returned by getters are unmodifiable; the world does not
 * change after initialisation.
 */
public class WorldRegistry {

    private final Map<String, Location> locationsMap;
    private final List<NPCCharacter> npcList;

    /**
     * Constructs the world with pre-built locations and NPCs. This is called
     * once by {@link GameEngine} at startup after data is loaded by a parser.
     */
    public WorldRegistry(Map<String, Location> locations, List<NPCCharacter> npcs) {
        if (locations == null) {
            throw new IllegalArgumentException("Locations cannot be null.");
        }
        if (npcs == null) {
            throw new IllegalArgumentException("NPC list cannot be null.");
        }
        this.locationsMap = Map.copyOf(locations);
        this.npcList = List.copyOf(npcs);
    }

    // ── Public accessors ──────────────────────────────────────────────────────
    /**
     * Returns the {@link Location} with the given name, or {@code null} if not
     * found.
     *
     * @param name the location name, e.g. {@code "Home"}
     * @return the matching location, or {@code null}
     */
    public Location getLocation(String name) {
        return locationsMap.get(name);
    }

    /**
     * Returns an unmodifiable view of all locations in the world.
     *
     * @return all locations
     */
    public Collection<Location> getAllLocations() {
        return Collections.unmodifiableCollection(locationsMap.values());
    }

    /**
     * Returns an unmodifiable list of all NPCs in the world.
     *
     * @return all NPCs
     */
    public List<NPCCharacter> getAllNPCs() {
        return Collections.unmodifiableList(npcList);
    }
}
