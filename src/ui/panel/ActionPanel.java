package ui.panel;

import Types.InteractionType;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import models.Location;
import models.NPCCharacter;
import models.furnitureactions.Furniture;

public class ActionPanel implements Panel {

    public enum Mode {
        MAIN, INTERACTABLES, SOCIALISE, CHANGE_LOCATION
    }

    private Mode mode = Mode.MAIN;
    private List<Furniture> availableFurniture = new ArrayList<>();
    private List<NPCCharacter> nearbyNPCs = new ArrayList<>();

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setFurniture(List<Furniture> furniture) {
        this.availableFurniture = furniture;
    }

    public void setNearbyNPCs(List<NPCCharacter> npcs) {
        this.nearbyNPCs = npcs;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public List<String> render() {
        return switch (mode) {
            case MAIN ->
                renderMain();
            case INTERACTABLES ->
                renderInteractables();
            case SOCIALISE ->
                renderSocialise();
            case CHANGE_LOCATION ->
                renderChangeLocation();
        };
    }

    private List<String> renderMain() {
        List<String> lines = new ArrayList<>();
        lines.add("Actions");
        lines.add("");
        lines.add("1. Interactables");
        lines.add("2. Socialise");
        lines.add("3. Change Location");
        lines.add("4. Exit Game");
        return lines;
    }

    private List<String> renderInteractables() {
        List<String> lines = new ArrayList<>();
        lines.add("Interactables");
        lines.add("");

        if (availableFurniture.isEmpty()) {
            lines.add("Nothing to interact with here.");
        } else {
            int i = 1;
            for (Furniture f : availableFurniture) {
                lines.add(i + ". " + f.getName());
                List<String> actions = f.getActionNames();
                for (int j = 0; j < actions.size(); j++) {
                    String prefix = (j == actions.size() - 1) ? "   └── " : "   ├── ";
                    lines.add(prefix + actions.get(j));
                }
                lines.add("");
                i++;
            }
        }

        lines.add("0. Back");
        return lines;
    }

    private List<String> renderSocialise() {
        List<String> lines = new ArrayList<>();
        lines.add("Socialise");
        lines.add("");

        if (nearbyNPCs.isEmpty()) {
            lines.add("No one around to socialise with.");
        } else {
            for (NPCCharacter npc : nearbyNPCs) {
                lines.add(npc.getName());
                int i = 1;
                for (InteractionType type : InteractionType.values()) {
                    lines.add("   " + i + ". " + type.getLabel());
                    i++;
                }
                lines.add("");
            }
        }

        lines.add("0. Back");
        return lines;
    }

    private List<String> renderChangeLocation() {
        List<String> lines = new ArrayList<>();
        lines.add("Change Location");
        lines.add("");

        int i = 1;
        for (Location loc : WorldRegistry.getInstance().getAllLocations()) {
            lines.add(i + ". " + loc.getLocationName());
            i++;
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }
}
