package models.furniture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import models.need.NeedType;
import models.skill.SkillType;
import org.junit.jupiter.api.Test;

class FurnitureTest {

    @Test
    void constructorStoresFields() {
        Furniture furniture = new Furniture("Bed", "Comfortable bed", 250.0);

        assertEquals("Bed", furniture.getName());
        assertEquals("Comfortable bed", furniture.getDescription());
        assertEquals(250.0, furniture.getPrice());
        assertTrue(furniture.getActions().isEmpty());
        assertTrue(furniture.getActionNames().isEmpty());
    }

    @Test
    void addActionIgnoresNull() {
        Furniture furniture = new Furniture("Desk", "Study desk", 100.0);

        furniture.addAction(null);

        assertTrue(furniture.getActions().isEmpty());
    }

    @Test
    void addActionStoresAndRetrievesByName() {
        Furniture furniture = new Furniture("Computer", "Gaming PC", 1500.0);
        FurnitureAction code = new FurnitureAction(
                "Code",
                "Practice programming",
                Map.of(NeedType.ENERGY, -5.0),
                Map.of(SkillType.PROGRAMMING, 20.0),
                0.0,
                2.0
        );

        furniture.addAction(code);

        assertEquals(1, furniture.getActions().size());
        assertEquals(List.of("Code"), furniture.getActionNames());
        assertEquals(code, furniture.getAction("Code"));
    }

    @Test
    void getActionReturnsNullWhenMissing() {
        Furniture furniture = new Furniture("Sofa", "Soft sofa", 200.0);

        assertNull(furniture.getAction("Sleep"));
    }

    @Test
    void performActionReturnsFalseWhenActionMissing() {
        Furniture furniture = new Furniture("TV", "Big TV", 500.0);
        models.character.SimCharacter sim = new models.character.SimCharacter(
                "Alex",
                20,
                testTypes.Gender.MALE,
                new models.location.Location("Home", new java.util.ArrayList<>())
        );

        assertFalse(furniture.performAction("Watch", sim));
    }

    @Test
    void performActionDelegatesToStoredAction() {
        Furniture furniture = new Furniture("Fridge", "Kitchen fridge", 400.0);
        FurnitureAction eat = new FurnitureAction(
                "Eat",
                "Have a meal",
                Map.of(NeedType.HUNGER, 10.0),
                Map.of(),
                5.0,
                1.0
        );
        models.character.SimCharacter sim = new models.character.SimCharacter(
                "Alex",
                20,
                testTypes.Gender.MALE,
                new models.location.Location("Home", new java.util.ArrayList<>())
        );

        furniture.addAction(eat);

        assertTrue(furniture.performAction("Eat", sim));
        assertEquals(995.0, sim.getMoney());
        assertEquals(90.0, sim.getNeed(NeedType.HUNGER).getValue());
    }
}