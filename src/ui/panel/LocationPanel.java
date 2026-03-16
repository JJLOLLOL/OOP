package ui.panel;

import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import models.Location;

public class LocationPanel implements Panel {

    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();
        lines.add("Change Location");
        lines.add("");

        List<Location> locations = new ArrayList<>(
                WorldRegistry.getInstance().getAllLocations());

        for (int i = 0; i < locations.size(); i++) {
            lines.add((i + 1) + ". " + locations.get(i).getLocationName());
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }
}
