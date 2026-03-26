package models.debuffs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import models.action.ActionType;
import models.character.SimCharacter;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import testTypes.Gender;

import org.junit.jupiter.api.Test;

class DebuffRegistryTest {

    private SimCharacter createSim() {
        return new SimCharacter("Alex", 20, Gender.MALE, new Location("Home", new ArrayList<>()));
    }

    @Test
    void applyNeedModifiersReturnsOriginalWhenNoDebuffIsActive() {
        SimCharacter sim = createSim();

        assertEquals(10.0, DebuffRegistry.applyNeedModifiers(sim, NeedType.ENERGY, 10.0));
        assertEquals(-3.0, DebuffRegistry.applyNeedModifiers(sim, NeedType.HUNGER, -3.0));
    }

    @Test
    void applyNeedModifiersAppliesHungerEnergyDebuff() {
        SimCharacter sim = createSim();
        sim.adjustNeed(NeedType.HUNGER, -70.0); // 10 => critical

        assertEquals(5.0, DebuffRegistry.applyNeedModifiers(sim, NeedType.ENERGY, 10.0));
        assertEquals(-4.0, DebuffRegistry.applyNeedModifiers(sim, NeedType.ENERGY, -4.0));
        assertEquals(8.0, DebuffRegistry.applyNeedModifiers(sim, NeedType.FUN, 8.0));
    }

    @Test
    void applySkillModifiersReturnsOriginalWhenNoDebuffIsActive() {
        SimCharacter sim = createSim();

        assertEquals(20.0, DebuffRegistry.applySkillModifiers(sim, SkillType.LOGIC, 20.0));
    }

    @Test
    void applySkillModifiersAppliesEnergySkillDebuff() {
        SimCharacter sim = createSim();
        sim.adjustNeed(NeedType.ENERGY, -70.0); // 10 => critical

        assertEquals(10.0, DebuffRegistry.applySkillModifiers(sim, SkillType.LOGIC, 20.0));
        assertEquals(-6.0, DebuffRegistry.applySkillModifiers(sim, SkillType.LOGIC, -6.0));
    }

    @Test
    void applyDecayModifiersCombinesAllActiveDebuffs() {
        SimCharacter sim = createSim();
        sim.adjustNeed(NeedType.HUNGER, -70.0);  // hunger critical
        sim.adjustNeed(NeedType.ENERGY, -70.0);  // energy critical
        sim.adjustNeed(NeedType.SOCIAL, -70.0);  // social critical

        assertEquals(4.0, DebuffRegistry.applyDecayModifiers(sim, NeedType.ENERGY, 1.0));
        assertEquals(2.0, DebuffRegistry.applyDecayModifiers(sim, NeedType.HUNGER, 1.0));
        assertEquals(4.0, DebuffRegistry.applyDecayModifiers(sim, NeedType.FUN, 1.0));
    }

    @Test
    void getInteractionBlockReasonReturnsNullWhenNoDebuffBlocks() {
        SimCharacter sim = createSim();

        assertNull(DebuffRegistry.getInteractionBlockReason(sim, ActionType.SOCIALISE));
        assertNull(DebuffRegistry.getInteractionBlockReason(sim, ActionType.EAT));
    }

    @Test
    void getInteractionBlockReasonReturnsHygieneBlockMessage() {
        SimCharacter sim = createSim();
        sim.adjustNeed(NeedType.HYGIENE, -70.0); // 10 => critical

        assertEquals("Your hygiene is too poor!",
                DebuffRegistry.getInteractionBlockReason(sim, ActionType.SOCIALISE));
        assertNull(DebuffRegistry.getInteractionBlockReason(sim, ActionType.SLEEP));
    }
}