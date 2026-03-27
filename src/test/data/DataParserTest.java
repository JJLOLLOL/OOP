package data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.need.NeedType;

class DataParserTest {

    @Test
    void loadWorldData_shouldReturnWorldData_whenResourcesExist() {
        WorldLoader parser = new WorldLoader();

        WorldData worldData = assertDoesNotThrow(parser::loadWorldData);

        assertNotNull(worldData);
        assertNotNull(worldData.getLocations());
        assertNotNull(worldData.getNpcs());
        assertNotNull(worldData.getShopInventory());
    }

    @Test
    void loadWorldData_shouldLoadAtLeastSomeLocations() {
        WorldLoader parser = new WorldLoader();

        WorldData worldData = parser.loadWorldData();

        assertFalse(worldData.getLocations().isEmpty());
    }

    @Test
    void parseEffects_shouldReturnEmptyMap_whenInputIsNull() throws Exception {
        WorldLoader parser = new WorldLoader();
        Method method = WorldLoader.class.getDeclaredMethod("parseEffects", String.class, java.util.function.Function.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<NeedType, Double> result = (Map<NeedType, Double>) method.invoke(
                parser,
                null,
                (java.util.function.Function<String, NeedType>) s -> NeedType.valueOf(s.toUpperCase())
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseEffects_shouldParseValidEffects() throws Exception {
        WorldLoader parser = new WorldLoader();
        Method method = WorldLoader.class.getDeclaredMethod("parseEffects", String.class, java.util.function.Function.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<NeedType, Double> result = (Map<NeedType, Double>) method.invoke(
                parser,
                "ENERGY,10;HUNGER,-5",
                (java.util.function.Function<String, NeedType>) s -> NeedType.valueOf(s.toUpperCase())
        );

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey(NeedType.ENERGY));
        assertTrue(result.containsKey(NeedType.HUNGER));
    }

    @Test
    void readFile_shouldThrowIOException_whenResourceDoesNotExist() throws Exception {
        WorldLoader parser = new WorldLoader();
        Method method = WorldLoader.class.getDeclaredMethod("readFile", String.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> method.invoke(parser, "data/does_not_exist.txt")
        );

        assertTrue(ex.getCause() instanceof IOException);
    }
}