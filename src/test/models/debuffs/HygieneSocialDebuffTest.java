package models.debuffs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import models.action.ActionType;
import models.character.SimCharacter;
import models.location.Location;
import models.need.NeedType;
import testTypes.Gender;

import org.junit.jupiter.api.Test;

class HygieneSocialDebuffTest {

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void isActiveWhenHygieneIsCritical() {
        SimCharacter sim = createSim();
        HygieneSocialDebuff debuff = new HygieneSocialDebuff();

        assertFalse(debuff.isActive(sim));

        sim.adjustNeed(NeedType.HYGIENE, -70.0);

        assertTrue(debuff.isActive(sim));
    }

    @Test
    void blockActionBlocksOnlySocialiseWhenActive() {
        SimCharacter sim = createSim();
        HygieneSocialDebuff debuff = new HygieneSocialDebuff();

        assertFalse(debuff.blockAction(sim, ActionType.SOCIALISE));

        sim.adjustNeed(NeedType.HYGIENE, -70.0);

        assertTrue(debuff.blockAction(sim, ActionType.SOCIALISE));
        assertFalse(debuff.blockAction(sim, ActionType.EAT));
    }

    @Test
    void getBlockMessageReturnsExpectedText() {
        SimCharacter sim = createSim();
        HygieneSocialDebuff debuff = new HygieneSocialDebuff();

        assertEquals("Your hygiene is too poor!", debuff.getBlockMessage(sim));
    }
}