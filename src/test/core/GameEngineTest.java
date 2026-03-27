package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import models.character.NPCCharacter;
import models.character.SimCharacter;
import models.location.House;
import models.location.Location;
import models.need.NeedType;
import types.Gender;
import ui.UITestSupport;

class GameEngineTest {

    @Test
    void handleInputRoutesCreateSimAndPlayingPhases() throws Exception {
        UITestSupport.resetPlayController(new data.ShopInventory(List.of(), List.of()));
        GameEngine engine = new GameEngine(engineWorld());
        GameState state = getField(engine, "state", GameState.class);
        WorldRegistry world = getField(engine, "world", WorldRegistry.class);
        controller.CreateSimController createSimController =
                getField(engine, "createSimController", controller.CreateSimController.class);

        UITestSupport.captureOutput(() -> invoke(engine, "handleInput",
                new Class<?>[]{String.class}, "1"));
        assertEquals(controller.CreateSimController.Step.NAME, createSimController.getStep());

        SimCharacter player = new SimCharacter("Alex", 25, Gender.MALE, world.getLocation("Home"));
        player.assignHouse((House) world.getLocation("Home"));
        state.addSim(player);
        state.setActivePlayer(player);
        state.setPhase(GameState.Phase.PLAYING);

        UITestSupport.captureOutput(() -> invoke(engine, "handleInput",
                new Class<?>[]{String.class}, "6"));
        assertSame(GameState.Phase.QUIT, state.getPhase());
    }

    @Test
    void tickAdvancesClockNeedsNotificationsAndNpcLocationsOnlyWhilePlaying() throws Exception {
        WorldRegistry world = engineWorld();
        GameEngine engine = new GameEngine(world);
        GameState state = getField(engine, "state", GameState.class);

        SimCharacter player = new SimCharacter("Alex", 25, Gender.MALE, world.getLocation("Home"));
        player.assignHouse((House) world.getLocation("Home"));
        state.addSim(player);
        state.setActivePlayer(player);

        double hungerBefore = player.getNeed(NeedType.HUNGER).getValue();
        NPCCharacter npc = world.getAllNPCs().get(0);

        invoke(engine, "tick", new Class<?>[]{double.class}, 0.5);
        assertEquals(0, state.getGameClock().getMinutes());
        assertEquals(hungerBefore, player.getNeed(NeedType.HUNGER).getValue());
        assertSame(world.getLocation("Home"), npc.getLocation());

        state.setPhase(GameState.Phase.PLAYING);

        invoke(engine, "tick", new Class<?>[]{double.class}, 0.5);

        assertEquals(1, state.getGameClock().getMinutes());
        assertTrue(player.getNeed(NeedType.HUNGER).getValue() < hungerBefore);
        assertSame(world.getLocation("Park"), npc.getLocation());
    }

    @Test
    void shutdownPrintsExitMessageAndStopsInputThread() throws Exception {
        GameEngine engine = new GameEngine(engineWorld());

        String output = UITestSupport.captureOutput(
                () -> invoke(engine, "shutdown", new Class<?>[0]));

        InputThread inputThread = getField(engine, "inputThread", InputThread.class);
        boolean running = getField(inputThread, "running", Boolean.class);

        assertTrue(output.contains("Game over. Thanks for playing!"));
        assertFalse(running);
    }

    private static WorldRegistry engineWorld() {
        House home = new House("Home", new ArrayList<>());
        Location park = new Location("Park", new ArrayList<>());

        TreeMap<Integer, Location> schedule = new TreeMap<>();
        schedule.put(800, home);
        schedule.put(801, park);
        NPCCharacter npc = new NPCCharacter("Taylor", 30, Gender.FEMALE, "Walker", schedule);

        Map<String, Location> locations = new LinkedHashMap<>();
        locations.put("Home", home);
        locations.put("Park", park);
        return new WorldRegistry(locations, List.of(npc));
    }

    private static Object invoke(Object target, String methodName, Class<?>[] parameterTypes,
            Object... args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    private static <T> T getField(Object target, String fieldName, Class<T> type) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(target));
    }
}
