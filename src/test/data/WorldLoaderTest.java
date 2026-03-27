package data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import core.ActionResult;
import core.GameClock;
import models.character.SimCharacter;
import models.career.CareerList;
import models.need.NeedType;
import types.Gender;

class WorldLoaderTest {

    @Test
    void loadWorldData_shouldPopulateLocationsNpcsAndShopInventory() {
        WorldData worldData = assertDoesNotThrow(() -> new WorldLoader().loadWorldData());

        assertNotNull(worldData);
        assertFalse(worldData.getLocations().isEmpty());
        assertFalse(worldData.getNpcs().isEmpty());
        assertNotNull(worldData.getShopInventory());
        assertFalse(worldData.getShopInventory().getAvailableFurniture().isEmpty());
        assertFalse(worldData.getShopInventory().getAvailableHouses().isEmpty());
        assertTrue(worldData.getLocations().containsKey("Home"));
        assertTrue(worldData.getLocations().containsKey("Office"));
    }

    @Test
    void loadWorldData_shouldInitializeWorkActionUsedBySimCharacter() {
        WorldData worldData = new WorldLoader().loadWorldData();
        SimCharacter sim = new SimCharacter("Tester", 25, Gender.MALE, worldData.getLocations().get("Home"));
        GameClock clock = new GameClock();

        sim.joinCareer(CareerList.SOFTWARE_DEVELOPER);
        clock.advanceHours(1.0);

        double moneyBefore = sim.getMoney();
        double energyBefore = sim.getNeed(NeedType.ENERGY).getValue();

        ActionResult result = sim.work(clock);

        assertTrue(result.isSuccess());
        assertTrue(sim.getMoney() > moneyBefore);
        assertTrue(sim.getNeed(NeedType.ENERGY).getValue() < energyBefore);
        assertTrue(clock.getHours() >= 17);
    }
}
