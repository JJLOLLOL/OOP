package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import models.character.SimCharacter;
import models.debuffs.DebuffRegistry;
import models.location.Location;
import models.need.NeedType;
import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class DebuffRegistryTest {

    private SimCharacter createSim() {
        return new SimCharacter("Riley", 23, "F", new Location("Home", new ArrayList<>()));
    }

    @Test
    void applyNeedModifiersHalvesEnergyRecoveryWhenHungerIsCritical() {
        SimCharacter sim = createSim();
        sim.getStats().adjustNeedRaw(NeedType.HUNGER, -65.0);

        double modified = DebuffRegistry.applyNeedModifiers(sim, NeedType.ENERGY, 20.0);

        assertEquals(10.0, modified);
    }

    @Test
    void applySkillModifiersHalvesPositiveSkillGainWhenEnergyIsCritical() {
        SimCharacter sim = createSim();
        sim.getStats().adjustNeedRaw(NeedType.ENERGY, -65.0);

        double modified = DebuffRegistry.applySkillModifiers(sim, SkillType.LOGIC, 20.0);

        assertEquals(10.0, modified);
    }

    @Test
    void applyDecayModifiersReflectsSocialAndHungerDebuffs() {
        SimCharacter sim = createSim();
        sim.getStats().adjustNeedRaw(NeedType.SOCIAL, -65.0);
        sim.getStats().adjustNeedRaw(NeedType.HUNGER, -65.0);

        assertEquals(6.0, DebuffRegistry.applyDecayModifiers(sim, NeedType.FUN, 3.0));
        assertEquals(16.0, DebuffRegistry.applyDecayModifiers(sim, NeedType.ENERGY, 8.0));
    }

    @Test
    void interactionBlockReasonAppearsOnlyWhenRelevantDebuffIsActive() {
        SimCharacter sim = createSim();

        assertNull(DebuffRegistry.getInteractionBlockReason(sim, "Socialise"));

        sim.getStats().adjustNeedRaw(NeedType.HYGIENE, -65.0);

        assertEquals("Your hygiene is too poor!", DebuffRegistry.getInteractionBlockReason(sim, "Socialise"));
        assertNull(DebuffRegistry.getInteractionBlockReason(sim, "Work"));
    }
}
