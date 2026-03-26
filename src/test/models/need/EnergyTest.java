package models.need;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EnergyTest {

    @Test
    void constructor_setsCorrectDefaults() {
        Energy energy = new Energy();

        assertEquals(NeedType.ENERGY, energy.getType());
        assertEquals("Energy", energy.getNeedName());
        assertEquals(80.0, energy.getValue());
        assertEquals(8.0, energy.getDecayRate());
        assertEquals(8.0, energy.getBaseDecayRate());
    }
}