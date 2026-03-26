package controller.play;

import controller.PlayController;
import models.location.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationChangeHandler implements PlayInputHandler {

    private List<Location> locations;

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

    @Override
    public void onEnter(PlayContext context) {
        this.locations = new ArrayList<>(context.getWorldRegistry().getAllLocations());
    }

    @Override
    public PlayController.Step getStep() {
        return PlayController.Step.CHANGE_LOCATION;
    }

}