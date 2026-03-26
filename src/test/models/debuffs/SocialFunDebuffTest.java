package models.debuffs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import models.character.SimCharacter;
import models.location.Location;
import models.need.NeedType;
import types.Gender;

import org.junit.jupiter.api.Test;

class SocialFunDebuffTest {

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void isActiveWhenSocialIsCritical() {
        SimCharacter sim = createSim();
        SocialFunDebuff debuff = new SocialFunDebuff();

        assertFalse(debuff.isActive(sim));

        sim.adjustNeed(NeedType.SOCIAL, -70.0);

        assertTrue(debuff.isActive(sim));
    }

    @Test
    void modifyNeedDecayDoublesOnlyFunDecay() {
        SimCharacter sim = createSim();
        SocialFunDebuff debuff = new SocialFunDebuff();

        assertEquals(4.0, debuff.modifyNeedDecay(sim, NeedType.FUN, 2.0));
        assertEquals(2.0, debuff.modifyNeedDecay(sim, NeedType.SOCIAL, 2.0));
        assertEquals(2.0, debuff.modifyNeedDecay(sim, NeedType.ENERGY, 2.0));
    }
}