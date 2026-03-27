package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import core.GameState;
import controller.play.InteractionHandler;
import controller.play.ShopHandler;
import controller.play.SocialHandler;
import models.furniture.FurnitureAction;
import models.need.NeedType;
import models.skill.SkillType;
import services.NotificationService;
import types.AchievementType;
import ui.UITestSupport;

class PlayControllerTest {

    @Test
    void handleInputMovesBetweenMainMenuStatesAndCanQuit() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        assertEquals(PlayController.Step.MAIN, fixture.playController.getActiveHandler().getStep());
        assertTrue(fixture.playController.handleInput("1", fixture.state, fixture.world));
        assertEquals(PlayController.Step.INTERACTABLES, fixture.playController.getActiveHandler().getStep());

        UITestSupport.Fixture quitFixture = UITestSupport.fixture();
        assertTrue(quitFixture.playController.handleInput("6", quitFixture.state, quitFixture.world));
        assertSame(GameState.Phase.QUIT, quitFixture.state.getPhase());
    }

    @Test
    void interactionAndSocialSelectionPopulateHandlerSelections() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        fixture.playController.handleInput("1", fixture.state, fixture.world);
        fixture.playController.handleInput("1", fixture.state, fixture.world);
        assertEquals(PlayController.Step.INTERACTABLE_ACTION, fixture.playController.getActiveHandler().getStep());
        assertSame(
                fixture.studyDesk,
                ((InteractionHandler) fixture.playController.getActiveHandler()).getSelectedFurniture());

        UITestSupport.Fixture socialFixture = UITestSupport.fixture();
        socialFixture.playController.handleInput("2", socialFixture.state, socialFixture.world);
        socialFixture.playController.handleInput("1", socialFixture.state, socialFixture.world);
        assertEquals(PlayController.Step.SOCIALISE_ACTION, socialFixture.playController.getActiveHandler().getStep());
        assertSame(
                socialFixture.roommate,
                ((SocialHandler) socialFixture.playController.getActiveHandler()).getSelectedCharacter());
    }

    @Test
    void shopHandlerExposesSelectedInventoryLists() {
        UITestSupport.Fixture fixture = UITestSupport.fixture();

        fixture.playController.handleInput("5", fixture.state, fixture.world);
        fixture.playController.handleInput("1", fixture.state, fixture.world);
        assertEquals(PlayController.Step.SHOP_HOUSES, fixture.playController.getActiveHandler().getStep());
        assertEquals(
                List.of(fixture.villa),
                ((ShopHandler) fixture.playController.getActiveHandler()).getHousesForSale());

        UITestSupport.Fixture furnitureFixture = UITestSupport.fixture();
        furnitureFixture.playController.handleInput("5", furnitureFixture.state, furnitureFixture.world);
        furnitureFixture.playController.handleInput("2", furnitureFixture.state, furnitureFixture.world);
        assertEquals(PlayController.Step.SHOP_FURNITURE, furnitureFixture.playController.getActiveHandler().getStep());
        assertEquals(
                List.of(furnitureFixture.lamp),
                ((ShopHandler) furnitureFixture.playController.getActiveHandler()).getFurnitureForSale());
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
                fixture.player, List.of(AchievementType.FIRST_JOB));
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

        assertFalse(fixture.playController.handleInput("x", fixture.state, fixture.world));
        assertEquals(PlayController.Step.MAIN, fixture.playController.getActiveHandler().getStep());
    }

    private static Object invokeStatic(Class<?> type, String methodName, Class<?>[] parameterTypes,
            Object... args) throws Exception {
        Method method = type.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(null, args);
    }
}
