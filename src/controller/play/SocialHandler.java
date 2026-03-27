package controller.play;

import controller.PlayController;
import core.GameState;
import models.action.ActionType;
import models.character.SimCharacter;
import models.debuffs.DebuffRegistry;
import models.need.NeedType;
import services.NotificationService;
import types.InteractionType;

import java.util.List;

/**
 * Handles character-to-character social interaction menus.
 */
public class SocialHandler implements PlayInputHandler {

    private PlayController.Step internalStep = PlayController.Step.SOCIALISE;
    private models.character.Character selectedCharacter;
    private List<models.character.Character> charactersAtLocation;

    /**
     * Routes input between target selection and interaction selection.
     *
     * @param input the player's raw input
     * @param context the gameplay context
     * @return {@code true} when a state transition occurred
     */
    @Override
    public boolean handleInput(String input, PlayContext context) {
        return switch (internalStep) {
            case SOCIALISE -> handleSocialiseList(input, context);
            case SOCIALISE_ACTION -> handleSocialiseAction(input, context);
            default -> false;
        };
    }

    /**
     * Resolves which nearby character the player wants to interact with.
     */
    private boolean handleSocialiseList(String input, PlayContext context) {
        if (input.equals("0")) {
            context.switchTo(HandlerType.MAIN_MENU);
            return true;
        }
        return PlayController.pickFromList(input, charactersAtLocation, idx -> {
            this.selectedCharacter = charactersAtLocation.get(idx);
            this.internalStep = PlayController.Step.SOCIALISE_ACTION;
        });
    }

    /**
     * Executes the chosen social interaction and applies any resulting need,
     * relationship, and achievement updates.
     */
    private boolean handleSocialiseAction(String input, PlayContext context) {
        if (input.equals("0")) {
            this.selectedCharacter = null;
            this.internalStep = PlayController.Step.SOCIALISE;
            onEnter(context); // re-fetch characters
            return true;
        }
        InteractionType[] types = InteractionType.values();
        return PlayController.pickFromList(input, List.of(types), idx -> {
            SimCharacter player = context.getActivePlayer();
            GameState state = context.getGameState();
            InteractionType chosen = types[idx];

            String blockReason = DebuffRegistry.getInteractionBlockReason(player, ActionType.SOCIALISE);
            if (blockReason != null) {
                NotificationService.add(player, selectedCharacter.getName() + " refused to interact! " + blockReason);
            } else {
                String result = state.getRelationshipService().interact(player, selectedCharacter, chosen);
                player.adjustNeed(NeedType.SOCIAL, chosen.getEffect());
                PlayController.addAchievementNotifications(
                        player,
                        state.getAchievementService().evaluateSocialAchievements(
                                player,
                                PlayController.getAllCharacters(state, context.getWorldRegistry()),
                                state.getRelationshipService()));
                NotificationService.add(player, result);
            }
            context.switchTo(HandlerType.MAIN_MENU);
        });
    }

    /**
     * Resets the social flow and snapshots characters at the active player's
     * location.
     *
     * @param context the gameplay context
     */
    @Override
    public void onEnter(PlayContext context) {
        this.internalStep = PlayController.Step.SOCIALISE;
        this.selectedCharacter = null;
        this.charactersAtLocation = PlayController.charsAt(context.getActivePlayer().getLocation(), context.getGameState(), context.getWorldRegistry());
    }

    /**
     * Returns the gameplay step currently represented by this handler.
     *
     * @return the current social sub-step
     */
    @Override
    public PlayController.Step getStep() {
        return internalStep;
    }

    // Accessors for view
    public models.character.Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public List<models.character.Character> getCharactersAtLocation() {
        return charactersAtLocation;
    }
}
