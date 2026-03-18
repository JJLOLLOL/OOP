package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.House;
import models.Location;
import models.NPCCharacter;
import models.actions.Furniture;
import models.actions.FurnitureFactory;

/**
 * Holds all static world data — locations and NPCs — built once at startup.
 *
 * <p>
 * Previously split across {@code WorldRegistry} (storage) and
 * {@code WorldBuilder} (construction). Merged here because the builder was only
 * ever called once, from this class, and is not reused anywhere else. Adding a
 * new location or NPC now requires editing only one file.
 *
 * <p>
 * All collections returned by getters are unmodifiable; the world does not
 * change after initialisation.
 */
public class WorldRegistry {

    private final Map<String, Location> locationsMap;
    private final List<NPCCharacter> npcList;

    /**
     * Constructs the world by building all locations and NPCs immediately. This
     * is called once by {@link GameEngine} at startup.
     */
    public WorldRegistry() {
        this.locationsMap = buildLocations();
        this.npcList = buildNPCs(locationsMap);
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

    // ══════════════════════════════════════════════════════════════════════════
    //  World construction
    //  To add a new location: create it below and put() it into locationsMap.
    //  To add a new NPC: create a schedule TreeMap and add the NPC to the list.
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Builds and returns the map of all locations, keyed by location name. Each
     * location is populated with its starting furniture.
     */
    private static Map<String, Location> buildLocations() {
        Map<String, Location> map = new HashMap<>();

        // ── Home ──────────────────────────────────────────────────────────────
        House home = new House("Home", new ArrayList<>(List.of(
                FurnitureFactory.createCheapMattress(),
                FurnitureFactory.createOldStove(),
                FurnitureFactory.createOldShower(),
                FurnitureFactory.createToilet(),
                FurnitureFactory.createOldCRTTV()
        )));
        map.put(home.getLocationName(), home);

        // ── Public locations ──────────────────────────────────────────────────
        addLocation(map, "Restaurant", List.of(
                FurnitureFactory.createRestaurantTable(),
                FurnitureFactory.createToilet()
        ));
        addLocation(map, "Gym", List.of(
                FurnitureFactory.createTreadmill(),
                FurnitureFactory.createDumbbells(),
                FurnitureFactory.createVendingMachine()
        ));
        addLocation(map, "Park", List.of(
                FurnitureFactory.createParkPath(),
                FurnitureFactory.createParkLake(),
                FurnitureFactory.createBicycle(),
                FurnitureFactory.createToilet(),
                FurnitureFactory.createPicnicTable()
        ));
        addLocation(map, "Cafe", List.of(
                FurnitureFactory.createCafeTable(),
                FurnitureFactory.createEspressoMachine(),
                FurnitureFactory.createJukeBox(),
                FurnitureFactory.createToilet()
        ));
        addLocation(map, "Library", List.of(
                FurnitureFactory.createBookshelf(),
                FurnitureFactory.createComputerDesk(),
                FurnitureFactory.createToilet()
        ));
        addLocation(map, "Club", List.of(
                FurnitureFactory.createBar(),
                FurnitureFactory.createDanceFloor(),
                FurnitureFactory.createToilet()
        ));
        addLocation(map, "Office", List.of(
                FurnitureFactory.createWorkDesk()
        ));

        return map;
    }

    /**
     * Builds and returns the list of all NPCs, each with a daily schedule that
     * references the already-built locations.
     *
     * <p>
     * Schedule keys are times in HHMM format (e.g. {@code 900} = 09:00,
     * {@code 1500} = 15:00). {@link services.NpcService} uses a floor-entry
     * lookup so NPCs stay at their last scheduled location until the next
     * entry.
     *
     * @param locs the fully built locations map
     * @return list of all NPCs
     */
    private static List<NPCCharacter> buildNPCs(Map<String, Location> locs) {
        List<NPCCharacter> npcs = new ArrayList<>();

        // Nicholas: park mornings, gym afternoons, cafe evenings
        npcs.add(new NPCCharacter("Nicholas", 30, "Male", "A friendly local.",
                schedule(locs, 900, "Park", 1500, "Gym", 2100, "Cafe")));

        // Jia Jing: library mornings, cafe lunch, gym evenings
        npcs.add(new NPCCharacter("Jia Jing", 25, "Female", "A busy student.",
                schedule(locs, 800, "Library", 1200, "Cafe", 1800, "Gym")));

        // Mahesha: gym early, library afternoon, park late night
        npcs.add(new NPCCharacter("Mahesha", 40, "Male", "A frequent gym-goer.",
                schedule(locs, 600, "Gym", 1400, "Library", 2200, "Park")));

        return npcs;
    }

    // ── Construction helpers ──────────────────────────────────────────────────
    /**
     * Creates a {@link Location} with the given name and furniture list and
     * adds it to the map.
     */
    private static void addLocation(Map<String, Location> map, String name,
            List<Furniture> furniture) {
        Location loc = new Location(name, new ArrayList<>(furniture));
        map.put(loc.getLocationName(), loc);
    }

    /**
     * Builds a {@link TreeMap} schedule from alternating time/location-name
     * pairs. Varargs format: {@code time1, name1, time2, name2, ...}
     *
     * @param locs the locations map to resolve names from
     * @param entries alternating int time and String location name
     * @return a TreeMap keyed by HHMM time
     */
    private static TreeMap<Integer, Location> schedule(Map<String, Location> locs,
            Object... entries) {
        TreeMap<Integer, Location> sched = new TreeMap<>();
        for (int i = 0; i < entries.length - 1; i += 2) {
            int time = (int) entries[i];
            String name = (String) entries[i + 1];
            sched.put(time, locs.get(name));
        }
        return sched;
    }
}
