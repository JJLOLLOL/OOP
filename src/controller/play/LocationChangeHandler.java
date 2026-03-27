package controller.play;

import controller.PlayController;
import models.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles moving the active sim between available locations.
 */
public class LocationChangeHandler implements PlayInputHandler {

    private List<Location> locations;

    /**
     * Parses a chosen destination and moves the active player there.
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
        return PlayController.pickFromList(input, locations, idx -> {
            context.getActivePlayer().setLocation(locations.get(idx));
            context.switchTo(HandlerType.MAIN_MENU);
        });
    }

    /**
     * Refreshes the list of selectable locations from the world registry.
     *
     * @param context the gameplay context
     */
    @Override
    public void onEnter(PlayContext context) {
        this.locations = new ArrayList<>(context.getWorldRegistry().getAllLocations());
    }

    /**
     * Returns the gameplay step currently represented by this handler.
     *
     * @return {@link controller.PlayController.Step#CHANGE_LOCATION}
     */
    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.CHANGE_LOCATION;
    }

}
