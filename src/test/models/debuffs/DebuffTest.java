package models.debuffs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import models.action.ActionType;
import models.character.SimCharacter;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import types.Gender;

import org.junit.jupiter.api.Test;

class DebuffTest {

    private static class TestDebuff implements Debuff {
        @Override
        public boolean isActive(SimCharacter sim) {
            return true;
        }
    }

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void defaultMethodsReturnUnmodifiedValues() {
        SimCharacter sim = createSim();
        Debuff debuff = new TestDebuff();

        assertEquals(10.0, debuff.modifyNeedChange(sim, NeedType.ENERGY, 10.0));
        assertEquals(15.0, debuff.modifySkillChange(sim, SkillType.LOGIC, 15.0));
        assertEquals(2.0, debuff.modifyNeedDecay(sim, NeedType.FUN, 2.0));
        assertFalse(debuff.blockAction(sim, ActionType.SOCIALISE));
        assertEquals("Action blocked due to a debuff.", debuff.getBlockMessage(sim));
    }
}