package models.furniture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Map;

import models.need.NeedType;
import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class ActivityInterfaceTest {

    private static class TestActivity implements ActivityInterface {
        @Override
        public String getName() {
            return "Study";
        }

        @Override
        public String getDescription() {
            return "Improves logic";
        }

        @Override
        public double getTimeRequired() {
            return 2.0;
        }

        @Override
        public Map<NeedType, Double> affectedNeedsByActionMap() {
            return Map.of(NeedType.ENERGY, -5.0);
        }

        @Override
        public Map<SkillType, Double> affectedSkillsByActionMap() {
            return Map.of(SkillType.LOGIC, 10.0);
        }

        @Override
        public double moneyDeducted() {
            return 15.0;
        }

        @Override
        public boolean perform(models.character.SimCharacter character) {
            return character != null;
        }
    }

    @Test
    void implementationReturnsConfiguredValues() {
        ActivityInterface activity = new TestActivity();

        assertEquals("Study", activity.getName());
        assertEquals("Improves logic", activity.getDescription());
        assertEquals(2.0, activity.getTimeRequired());
        assertEquals(Map.of(NeedType.ENERGY, -5.0), activity.affectedNeedsByActionMap());
        assertEquals(Map.of(SkillType.LOGIC, 10.0), activity.affectedSkillsByActionMap());
        assertEquals(15.0, activity.moneyDeducted());
    }

    @Test
    void performReturnsTrueForNonNullCharacter() {
        ActivityInterface activity = new TestActivity();
        Object result = activity.perform(new models.character.SimCharacter(
                "Alex",
                20,
                types.Gender.MALE,
                new models.location.Location("Home", new java.util.ArrayList<>())));

        assertSame(Boolean.TRUE, result);
    }
}