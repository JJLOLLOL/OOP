package controller.play;

import controller.PlayController;
import models.character.SimCharacter;

import java.util.List;

public class SwitchCharacterHandler implements PlayInputHandler {

    private List<SimCharacter> sims;

    @Override
    public boolean handleInput(String input, PlayContext context) {
        if (input.equals("0")) {
            context.switchTo(HandlerType.MAIN_MENU);
            return true;
        }
        return PlayController.pickFromList(input, sims, idx -> {
            context.getGameState().setActivePlayer(sims.get(idx));
            context.switchTo(HandlerType.MAIN_MENU);
        });
    }

    @Override
    public void onEnter(PlayContext context) {
        this.sims = context.getGameState().getSims();
    }

    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.SWITCH_CHARACTER;
    }
}