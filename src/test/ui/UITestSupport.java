package ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import controller.PlayController;
import core.GameState;
import core.WorldRegistry;
import data.ShopInventory;
import models.character.NPCCharacter;
import models.character.SimCharacter;
import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.location.House;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import types.Gender;

public final class UITestSupport {

    private UITestSupport() {
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static final class Fixture {
        public final House home;
        public final Location park;
        public final Location cafe;
        public final Furniture studyDesk;
        public final Furniture arcadeMachine;
        public final SimCharacter player;
        public final SimCharacter roommate;
        public final NPCCharacter npc;
        public final WorldRegistry world;
        public final GameState state;
        public final House villa;
        public final Furniture lamp;
        public final ShopInventory shopInventory;
        public final PlayController playController;

        private Fixture(House home, Location park, Location cafe, Furniture studyDesk,
                Furniture arcadeMachine, SimCharacter player, SimCharacter roommate,
                NPCCharacter npc, WorldRegistry world, GameState state, House villa,
                Furniture lamp, ShopInventory shopInventory, PlayController playController) {
            this.home = home;
            this.park = park;
            this.cafe = cafe;
            this.studyDesk = studyDesk;
            this.arcadeMachine = arcadeMachine;
            this.player = player;
            this.roommate = roommate;
            this.npc = npc;
            this.world = world;
            this.state = state;
            this.villa = villa;
            this.lamp = lamp;
            this.shopInventory = shopInventory;
            this.playController = playController;
        }
    }

    public static Fixture fixture() {
        House home = new House("Home", new ArrayList<>());

        Furniture studyDesk = new Furniture("Study Desk", "A quiet place to focus.", 250.0);
        studyDesk.addAction(new FurnitureAction(
                "Write",
                "Draft a short story.",
                Map.of(NeedType.FUN, -10.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.WRITING, 15.0),
                12.0,
                1.5));
        studyDesk.addAction(new FurnitureAction(
                "Code",
                "Ship a small feature.",
                Map.of(NeedType.ENERGY, -8.0),
                Map.of(SkillType.PROGRAMMING, 20.0, SkillType.LOGIC, 10.0),
                0.0,
                2.0));

        Furniture arcadeMachine = new Furniture("Arcade Machine", "Old but still playable.", 400.0);
        arcadeMachine.addAction(new FurnitureAction(
                "Play",
                "Relax at the arcade.",
                Map.of(NeedType.FUN, 20.0, NeedType.ENERGY, -5.0),
                Map.of(SkillType.CREATIVITY, 5.0),
                3.0,
                0.5));

        home.addFurniture(studyDesk);
        home.addFurniture(arcadeMachine);

        Location park = new Location("Park", new ArrayList<>());
        Location cafe = new Location("Cafe", new ArrayList<>());

        SimCharacter player = new SimCharacter("Alex", 25, Gender.MALE, home);
        player.assignHouse(home);
        SimCharacter roommate = new SimCharacter("Jamie", 22, Gender.FEMALE, home);
        roommate.assignHouse(home);

        TreeMap<Integer, Location> schedule = new TreeMap<>();
        schedule.put(800, home);
        schedule.put(1200, park);
        NPCCharacter npc = new NPCCharacter(
                "Taylor",
                30,
                Gender.FEMALE,
                "Reads by the window.",
                schedule);

        home.addNpcCharacter(npc);

        Map<String, Location> locations = new LinkedHashMap<>();
        locations.put(home.getLocationName(), home);
        locations.put(park.getLocationName(), park);
        locations.put(cafe.getLocationName(), cafe);
        WorldRegistry world = new WorldRegistry(locations, List.of(npc));

        GameState state = new GameState();
        state.addSim(player);
        state.addSim(roommate);
        state.setActivePlayer(player);

        House villa = new House("Villa", new ArrayList<>(), 2500.0, 2.0, 3);
        Furniture lamp = new Furniture("Lamp", "Warm ambient light.", 80.0);
        ShopInventory shopInventory = new ShopInventory(List.of(villa), List.of(lamp));
        PlayController playController = new PlayController(shopInventory);

        return new Fixture(home, park, cafe, studyDesk, arcadeMachine, player, roommate,
                npc, world, state, villa, lamp, shopInventory, playController);
    }

    public static void resetRendererLayout() {
        Renderer.LEFT_W = Renderer.MIN_COL_W;
        Renderer.MID_W = Renderer.MIN_COL_W;
        Renderer.SKILLS_W = Renderer.MIN_COL_W;
        Renderer.NOTIF_W = Renderer.MIN_COL_W;
        Renderer.INNER_W = 4 * (Renderer.MIN_COL_W + 2) + 3;
    }

    public static String captureOutput(ThrowingRunnable action) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public static String plain(String text) {
        return ConsoleUtils.stripAnsi(text);
    }

    public static String withoutClearScreen(String text) {
        return text.replace("\033[H\033[2J", "");
    }

    public static String findLineContaining(List<String> lines, String fragment) {
        return lines.stream()
                .filter(line -> plain(line).contains(fragment))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing line containing: " + fragment));
    }
}
