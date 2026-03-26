package models.debuffs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import models.character.SimCharacter;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import testTypes.Gender;

import org.junit.jupiter.api.Test;

class EnergySkillDebuffTest {

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void isActiveWhenEnergyIsCritical() {
        SimCharacter sim = createSim();
        EnergySkillDebuff debuff = new EnergySkillDebuff();

        assertFalse(debuff.isActive(sim));

        sim.adjustNeed(NeedType.ENERGY, -70.0);

        assertTrue(debuff.isActive(sim));
    }

    @Test
    void modifySkillChangeHalvesPositiveGainOnly() {
        SimCharacter sim = createSim();
        EnergySkillDebuff debuff = new EnergySkillDebuff();

        assertEquals(5.0, debuff.modifySkillChange(sim, SkillType.LOGIC, 10.0));
        assertEquals(0.0, debuff.modifySkillChange(sim, SkillType.LOGIC, 0.0));
        assertEquals(-4.0, debuff.modifySkillChange(sim, SkillType.LOGIC, -4.0));
    }
}