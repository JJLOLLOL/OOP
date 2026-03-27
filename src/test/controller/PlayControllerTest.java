package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.GameState;
import models.career.CareerList;
import models.furniture.FurnitureAction;
import models.need.NeedType;
import models.skill.SkillType;
import services.NotificationService;
import types.AchievementList;
import ui.UITestSupport;

class PlayControllerTest {

    @BeforeEach
    void setUp() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        UITestSupport.resetPlayController(fixture.shopInventory);
    }

    @Test
    void handleInputMovesBetweenMainMenuStatesAndCanQuit() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        assertEquals(PlayController.Step.MAIN, PlayController.getStep());
        assertTrue(PlayController.handleInput("1", fixture.state, fixture.world));
        assertEquals(PlayController.Step.INTERACTABLES, PlayController.getStep());

        UITestSupport.resetPlayController(fixture.shopInventory);
        assertTrue(PlayController.handleInput("6", fixture.state, fixture.world));
        assertSame(GameState.Phase.QUIT, fixture.state.getPhase());
    }

    @Test
    void interactionAndSocialSelectionPopulateStaticSelections() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        PlayController.handleInput("1", fixture.state, fixture.world);
        PlayController.handleInput("1", fixture.state, fixture.world);
        assertEquals(PlayController.Step.INTERACTABLE_ACTION, PlayController.getStep());
        assertSame(fixture.studyDesk, PlayController.getSelectedFurniture());

        UITestSupport.resetPlayController(fixture.shopInventory);
        PlayController.handleInput("2", fixture.state, fixture.world);
        PlayController.handleInput("1", fixture.state, fixture.world);
        assertEquals(PlayController.Step.SOCIALISE_ACTION, PlayController.getStep());
        assertSame(fixture.roommate, PlayController.getSelectedCharacter());
    }

    @Test
    void shopRelatedStateAndCareerAccessorsReturnExpectedValues() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        PlayController.handleInput("5", fixture.state, fixture.world);
        PlayController.handleInput("1", fixture.state, fixture.world);
        assertEquals(PlayController.Step.SHOP_HOUSES, PlayController.getStep());
        assertEquals(List.of(fixture.villa), PlayController.getCurrentHouses());

        UITestSupport.resetPlayController(fixture.shopInventory);
        PlayController.handleInput("5", fixture.state, fixture.world);
        PlayController.handleInput("2", fixture.state, fixture.world);
        assertEquals(PlayController.Step.SHOP_FURNITURE, PlayController.getStep());
        assertEquals(List.of(fixture.lamp), PlayController.getCurrentFurniture());

        assertFalse(PlayController.getAvailableCareers().contains(CareerList.JOBLESS));
        assertTrue(PlayController.getAvailableCareers().contains(CareerList.SOFTWARE_DEVELOPER));
    }

    @Test
    void charsAtAndPrivateHelpersBehaveAsExpected() throws Exception {
        UITestSupport.Fixture fixture = UITestSupport.fixture();
        FurnitureAction action = new FurnitureAction(
                "Practice",
                "Practice programming.",
                Map.of(NeedType.ENERGY, -5.0),
                Map.of(SkillType.PROGRAMMING, 10.0),
                0.0,
                1.0);

        invokeStatic(PlayController.class, "addAchievementNotifications",
                new Class<?>[]{models.character.SimCharacter.class, List.class},
                fixture.player, List.of(AchievementList.FIRST_JOB));
        invokeStatic(PlayController.class, "addSkillAchievementNotifications",
                new Class<?>[]{models.character.SimCharacter.class, models.furniture.FurnitureAction.class, GameState.class},
                fixture.player, action, fixture.state);

        String output = UITestSupport.captureOutput(() -> {
            boolean result = (boolean) invokeStatic(PlayController.class, "pickFromList",
                    new Class<?>[]{String.class, List.class, Class.forName("controller.PlayController$IndexAction")},
                    "4", List.of("a", "b"),
                    java.lang.reflect.Proxy.newProxyInstance(
                            PlayController.class.getClassLoader(),
                            new Class<?>[]{Class.forName("controller.PlayController$IndexAction")},
                            (proxy, method, args) -> null));
            assertFalse(result);
        });

        @SuppressWarnings("unchecked")
        List<models.character.Character> allCharacters =
                (List<models.character.Character>) invokeStatic(PlayController.class, "getAllCharacters",
                        new Class<?>[]{GameState.class, core.WorldRegistry.class},
                        fixture.state, fixture.world);

        assertEquals(List.of(fixture.roommate, fixture.npc),
                PlayController.charsAt(fixture.home, fixture.state, fixture.world));
        assertEquals(3, allCharacters.size());
        assertTrue(NotificationService.get(fixture.player).contains("Achievement unlocked: First Job"));
        assertTrue(NotificationService.get(fixture.player).contains("Achievement unlocked: First Programming"));
        assertTrue(output.contains("Enter a number from the list, or 0 to go back."));
    }

    @Test
    void invalidMainMenuInputReturnsFalseAndKeepsStep() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        assertFalse(PlayController.handleInput("x", fixture.state, fixture.world));
        assertEquals(PlayController.Step.MAIN, PlayController.getStep());
        assertNull(PlayController.getSelectedFurniture());
    }

    private static Object invokeStatic(Class<?> type, String methodName, Class<?>[] parameterTypes,
            Object... args) throws Exception {
        Method method = type.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(null, args);
    }
}
