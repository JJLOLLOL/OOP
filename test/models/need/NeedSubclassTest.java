package models.need;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import models.character.SimCharacter;
import models.location.Location;
import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class NeedSubclassTest {

    private SimCharacter createSim() {
        return new SimCharacter("Morgan", 24, "F", new Location("Home", new ArrayList<>()));
    }

    @Test
    void energyCriticalLowReducesHunger() {
        SimCharacter sim = createSim();
        double hungerBefore = sim.getNeed(NeedType.HUNGER).getValue();

        new Energy().onCriticallyLow(sim);

        assertEquals(hungerBefore - 5.0, sim.getNeed(NeedType.HUNGER).getValue());
    }

    @Test
    void hygieneCriticalLowReducesSocial() {
        SimCharacter sim = createSim();
        double socialBefore = sim.getNeed(NeedType.SOCIAL).getValue();

        new Hygiene().onCriticallyLow(sim);

        assertEquals(socialBefore - 10.0, sim.getNeed(NeedType.SOCIAL).getValue());
    }

    @Test
    void socialCriticalLowReducesEnergy() {
        SimCharacter sim = createSim();
        double energyBefore = sim.getNeed(NeedType.ENERGY).getValue();

        new Social().onCriticallyLow(sim);

        assertEquals(energyBefore - 10.0, sim.getNeed(NeedType.ENERGY).getValue());
    }

    @Test
    void funCriticalLowReducesCharismaXp() {
        SimCharacter sim = createSim();
        double charismaXpBefore = sim.getStats().getSkillXp(SkillType.CHARISMA);

        new Fun().onCriticallyLow(sim);

        assertEquals(charismaXpBefore, sim.getStats().getSkillXp(SkillType.CHARISMA));
    }

    @Test
    void hungerCriticalLowLeavesNeedsUnchanged() {
        SimCharacter sim = createSim();
        double hungerBefore = sim.getNeed(NeedType.HUNGER).getValue();

        new Hunger().onCriticallyLow(sim);

        assertEquals(hungerBefore, sim.getNeed(NeedType.HUNGER).getValue());
    }
}
