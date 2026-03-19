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

        // ── Existing NPCs ─────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Nicholas", 30, "Male", "A friendly local.",
                schedule(locs, 0, "Park", 900, "Park", 1500, "Gym", 2100, "Cafe")));

        npcs.add(new NPCCharacter("Jia Jing", 25, "Female", "A busy student.",
                schedule(locs, 0, "Library", 800, "Library", 1200, "Cafe", 1800, "Gym")));

        npcs.add(new NPCCharacter("Mahesha", 40, "Male", "A frequent gym-goer.",
                schedule(locs, 0, "Gym", 600, "Gym", 1400, "Library", 2200, "Park")));

        // ── Restaurant ────────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Priya", 34, "Female", "A marketing executive who lunches late.",
                schedule(locs, 0, "Restaurant", 800, "Cafe", 1000, "Office", 1900, "Restaurant")));

        npcs.add(new NPCCharacter("Damien", 28, "Male", "A personal trainer who eats big.",
                schedule(locs, 0, "Restaurant", 700, "Gym", 1300, "Restaurant", 2200, "Club")));

        npcs.add(new NPCCharacter("Soo-Yeon", 31, "Female", "A food blogger with a love of reading.",
                schedule(locs, 0, "Restaurant", 900, "Park", 1200, "Restaurant", 1600, "Library")));

        // ── Gym ───────────────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Tariq", 27, "Male", "A disciplined software engineer.",
                schedule(locs, 0, "Gym", 600, "Gym", 900, "Office", 1800, "Park")));

        npcs.add(new NPCCharacter("Ingrid", 22, "Female", "A fitness influencer between shoots.",
                schedule(locs, 0, "Gym", 800, "Cafe", 1400, "Gym", 2000, "Restaurant")));

        npcs.add(new NPCCharacter("Bayo", 35, "Male", "An amateur boxer who reads philosophy.",
                schedule(locs, 0, "Gym", 700, "Gym", 1500, "Library", 2300, "Club")));

        // ── Park ──────────────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Clara", 29, "Female", "A freelance illustrator who sketches outdoors.",
                schedule(locs, 0, "Park", 730, "Park", 1100, "Cafe", 1700, "Library")));

        npcs.add(new NPCCharacter("Reuben", 45, "Male", "A retired teacher who stays active.",
                schedule(locs, 0, "Park", 800, "Park", 1200, "Restaurant", 1500, "Gym")));

        npcs.add(new NPCCharacter("Min-Ji", 23, "Female", "A night-owl dancer who jogs to wake up.",
                schedule(locs, 0, "Club", 1400, "Park", 1900, "Cafe", 2200, "Club")));

        // ── Cafe ──────────────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Oliver", 38, "Male", "A remote consultant glued to his laptop.",
                schedule(locs, 0, "Cafe", 830, "Cafe", 1500, "Park", 1930, "Restaurant")));

        npcs.add(new NPCCharacter("Fatima", 26, "Female", "A PhD candidate fuelled entirely by espresso.",
                schedule(locs, 0, "Cafe", 900, "Cafe", 1300, "Library", 1900, "Cafe")));

        npcs.add(new NPCCharacter("Leon", 24, "Male", "A musician who works on lyrics over coffee.",
                schedule(locs, 0, "Cafe", 800, "Gym", 1300, "Cafe", 2100, "Club")));

        // ── Library ───────────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Amara", 33, "Female", "A novelist working on her second book.",
                schedule(locs, 0, "Library", 900, "Library", 1300, "Restaurant", 1500, "Library")));

        npcs.add(new NPCCharacter("Henrik", 52, "Male", "A retired professor who can't stop researching.",
                schedule(locs, 0, "Library", 800, "Cafe", 1200, "Library", 1800, "Park")));

        npcs.add(new NPCCharacter("Yuki", 20, "Female", "A quiet university student.",
                schedule(locs, 0, "Library", 900, "Library", 1500, "Gym", 2000, "Cafe")));

        // ── Club ──────────────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Marco", 26, "Male", "A bartender on his nights off.",
                schedule(locs, 0, "Club", 1100, "Cafe", 1800, "Restaurant", 2200, "Club")));

        npcs.add(new NPCCharacter("Zara", 21, "Female", "A university student who lives for weekends.",
                schedule(locs, 0, "Club", 1400, "Gym", 1900, "Cafe", 2300, "Club")));

        npcs.add(new NPCCharacter("Desmond", 30, "Male", "An accountant who unwinds hard.",
                schedule(locs, 0, "Club", 800, "Park", 1000, "Office", 2100, "Club")));

        // ── Office ────────────────────────────────────────────────────────────────
        npcs.add(new NPCCharacter("Nadia", 37, "Female", "A project manager always on deadline.",
                schedule(locs, 0, "Office", 800, "Cafe", 1000, "Office", 1900, "Restaurant")));

        npcs.add(new NPCCharacter("Kwame", 41, "Male", "A senior analyst who mentors junior staff.",
                schedule(locs, 0, "Office", 630, "Gym", 900, "Office", 1800, "Library")));

        npcs.add(new NPCCharacter("Sophie", 32, "Female", "A UX designer who needs fresh air to think.",
                schedule(locs, 0, "Office", 900, "Office", 1200, "Restaurant", 1600, "Park")));

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
