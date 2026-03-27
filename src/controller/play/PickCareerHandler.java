package controller.play;

import controller.PlayController;
import core.GameState;
import models.career.CareerList;
import models.character.SimCharacter;
import services.NotificationService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the career-picking submenu shown when a jobless sim uses the work
 * desk.
 */
public class PickCareerHandler implements PlayInputHandler {

    private static final List<CareerList> AVAILABLE_CAREERS = Arrays.stream(CareerList.values())
            .filter(c -> c != CareerList.JOBLESS)
            .collect(Collectors.toList());

    /**
     * Starts the selected career and emits any newly unlocked career
     * achievements.
     *
     * @param input the player's raw menu selection
     * @param context the gameplay context
     * @return {@code true} when the selection succeeds
     */
    @Override
    public boolean handleInput(String input, PlayContext context) {
        if (input.equals("0")) {
            context.switchTo(HandlerType.MAIN_MENU);
            return true;
        }
        return PlayController.pickFromList(input, AVAILABLE_CAREERS, idx -> {
            SimCharacter player = context.getActivePlayer();
            GameState state = context.getGameState();
            CareerList chosen = AVAILABLE_CAREERS.get(idx);
            player.joinCareer(chosen);
            PlayController.addAchievementNotifications(
                    player,
                    state.getAchievementService().evaluateCareerAchievements(player));
            NotificationService.add(player, "Career started: " + chosen.getTitle()
                    + ". Head to the Office to work!");
            context.switchTo(HandlerType.MAIN_MENU);
        });
    }

    /**
     * Performs no setup because the career list is static.
     *
     * @param context the gameplay context
     */
    @Override
    public void onEnter(PlayContext context) {
        // No setup needed
    }

    /**
     * Returns the gameplay step currently represented by this handler.
     *
     * @return {@link controller.PlayController.Step#PICK_CAREER}
     */
    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.PICK_CAREER;
    }
}
