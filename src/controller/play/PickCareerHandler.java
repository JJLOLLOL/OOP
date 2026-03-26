package controller.play;

import controller.PlayController;
import core.GameState;
import models.career.CareerList;
import models.character.SimCharacter;
import services.NotificationService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PickCareerHandler implements PlayInputHandler {

    private static final List<CareerList> AVAILABLE_CAREERS = Arrays.stream(CareerList.values())
            .filter(c -> c != CareerList.JOBLESS)
            .collect(Collectors.toList());

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

    @Override
    public void onEnter(PlayContext context) {
        // No setup needed
    }

    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.PICK_CAREER;
    }
}