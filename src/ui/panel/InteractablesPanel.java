package ui.panel;

import java.util.ArrayList;
import java.util.List;
import models.furnitureactions.Furniture;

public class InteractablesPanel implements Panel {

    private List<Furniture> furniture = new ArrayList<>();
    private Furniture selectedFurniture = null;

    public void setFurniture(List<Furniture> furniture) {
        this.furniture = furniture;
        this.selectedFurniture = null; // reset selection on location change
    }

    public void selectFurniture(Furniture f) {
        this.selectedFurniture = f;
    }

    public void clearSelection() {
        this.selectedFurniture = null;
    }

    @Override
    public List<String> render() {
        return selectedFurniture == null ? renderFurnitureList() : renderActionList();
    }

    private List<String> renderFurnitureList() {
        List<String> lines = new ArrayList<>();
        lines.add("Interactables");
        lines.add("");

        if (furniture.isEmpty()) {
            lines.add("Nothing to interact with here.");
        } else {
            for (int i = 0; i < furniture.size(); i++) {
                lines.add((i + 1) + ". " + furniture.get(i).getName());
            }
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }

    private List<String> renderActionList() {
        List<String> lines = new ArrayList<>();
        lines.add(selectedFurniture.getName());
        lines.add("");

        List<String> actions = selectedFurniture.getActionNames();
        if (actions.isEmpty()) {
            lines.add("No actions available.");
        } else {
            for (int i = 0; i < actions.size(); i++) {
                lines.add((i + 1) + ". " + actions.get(i));
            }
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }
}
