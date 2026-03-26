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

class FatigueDecayDebuffTest {

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void isActiveWhenEnergyIsCritical() {
        SimCharacter sim = createSim();
        FatigueDecayDebuff debuff = new FatigueDecayDebuff();

        assertFalse(debuff.isActive(sim));

        sim.adjustNeed(NeedType.ENERGY, -70.0);

        assertTrue(debuff.isActive(sim));
    }

    @Test
    void modifyNeedDecayDoublesEnergyAndAddsOneToOtherNeeds() {
        SimCharacter sim = createSim();
        FatigueDecayDebuff debuff = new FatigueDecayDebuff();

        assertEquals(4.0, debuff.modifyNeedDecay(sim, NeedType.ENERGY, 2.0));
        assertEquals(3.0, debuff.modifyNeedDecay(sim, NeedType.FUN, 2.0));
        assertEquals(3.0, debuff.modifyNeedDecay(sim, NeedType.HUNGER, 2.0));
    }
}