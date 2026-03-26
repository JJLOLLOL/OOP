package models.debuffs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import models.character.SimCharacter;
import models.location.Location;
import models.need.NeedType;
import testTypes.Gender;

import org.junit.jupiter.api.Test;

class HungerEnergyDebuffTest {

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void isActiveWhenHungerIsCritical() {
        SimCharacter sim = createSim();
        HungerEnergyDebuff debuff = new HungerEnergyDebuff();

        assertFalse(debuff.isActive(sim));

        sim.adjustNeed(NeedType.HUNGER, -70.0);

        assertTrue(debuff.isActive(sim));
    }

    @Test
    void modifyNeedChangeHalvesPositiveEnergyRecoveryOnly() {
        SimCharacter sim = createSim();
        HungerEnergyDebuff debuff = new HungerEnergyDebuff();

        assertEquals(5.0, debuff.modifyNeedChange(sim, NeedType.ENERGY, 10.0));
        assertEquals(-4.0, debuff.modifyNeedChange(sim, NeedType.ENERGY, -4.0));
        assertEquals(10.0, debuff.modifyNeedChange(sim, NeedType.FUN, 10.0));
    }

    @Test
    void modifyNeedDecayDoublesOnlyEnergyDecay() {
        SimCharacter sim = createSim();
        HungerEnergyDebuff debuff = new HungerEnergyDebuff();

        assertEquals(6.0, debuff.modifyNeedDecay(sim, NeedType.ENERGY, 3.0));
        assertEquals(3.0, debuff.modifyNeedDecay(sim, NeedType.HUNGER, 3.0));
    }
}