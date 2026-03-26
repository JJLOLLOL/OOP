package controller.play;

import controller.PlayController;
import core.ActionResult;
import core.GameState;
import models.character.SimCharacter;
import models.furniture.Furniture;
import services.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class InteractionHandler implements PlayInputHandler {

    private PlayController.Step internalStep = PlayController.Step.INTERACTABLES;
    private Furniture selectedFurniture;
    private List<Furniture> interactables;
    private List<String> sortedActionNames;

    @Override
    public boolean handleInput(String input, PlayContext context) {
        return switch (internalStep) {
            case INTERACTABLES -> handleInteractablesList(input, context);
            case INTERACTABLE_ACTION -> handleInteractableAction(input, context);
            default -> false; // Should not happen
        };
    }

    private boolean handleInteractablesList(String input, PlayContext context) {
        if (input.equals("0")) {
            context.switchTo(HandlerType.MAIN_MENU);
            return true;
        }
        return PlayController.pickFromList(input, interactables, idx -> {
            this.selectedFurniture = interactables.get(idx);
            this.sortedActionNames = new ArrayList<>(selectedFurniture.getActionNames());
            this.sortedActionNames.sort(String::compareTo);
            this.internalStep = PlayController.Step.INTERACTABLE_ACTION;
        });
    }

    private boolean handleInteractableAction(String input, PlayContext context) {
        if (input.equals("0")) {
            this.selectedFurniture = null;
            this.sortedActionNames = null;
            this.internalStep = PlayController.Step.INTERACTABLES;
            onEnter(context); // Re-fetch the list of interactables
            return true;
        }

        return PlayController.pickFromList(input, sortedActionNames, idx -> {
            String actionName = sortedActionNames.get(idx);
            SimCharacter player = context.getActivePlayer();
            GameState state = context.getGameState();

            // Intercept the Work Desk action
            if ("Work Desk".equals(selectedFurniture.getName()) && "Work".equals(actionName)) {
                if (player.isJobless()) {
                    context.switchTo(HandlerType.PICK_CAREER);
                } else {
                    ActionResult result = player.work(state.getGameClock());
                    PlayController.addAchievementNotifications(
                            player,
                            state.getAchievementService().evaluateWorkAchievements(player));
                    NotificationService.add(player, result.getMessage());
                    context.switchTo(HandlerType.MAIN_MENU);
                }
            } else {
                models.furniture.FurnitureAction action = selectedFurniture.getAction(actionName);
                boolean ok = (action != null) && action.perform(player, state.getGameClock());
                if (!ok) {
                    NotificationService.add(player, "Action failed: not enough money or needs too low.");
                } else if (action != null) {
                    PlayController.addSkillAchievementNotifications(player, action, state);
                }
                context.switchTo(HandlerType.MAIN_MENU);
            }
        });
    }

    @Override
    public void onEnter(PlayContext context) {
        this.internalStep = PlayController.Step.INTERACTABLES;
        this.selectedFurniture = null;
        this.sortedActionNames = null;
        this.interactables = context.getActivePlayer().getLocation().getFurnitureViews();
    }

    @Override
    public PlayController.Step getStep() {
        return internalStep;
    }

    // Accessors for the view
    public Furniture getSelectedFurniture() {
        return selectedFurniture;
    }

    public List<String> getSortedActionNames() {
        return sortedActionNames;
    }
}