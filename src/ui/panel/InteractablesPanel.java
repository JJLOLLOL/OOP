package ui.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.furnitureactions.Furniture;
import models.furnitureactions.FurnitureAction;

public class InteractablesPanel implements Panel {

    private List<Furniture> furniture = new ArrayList<>();
    private Furniture selectedFurniture = null;

    public void setFurniture(List<Furniture> furniture) {
        if (!furniture.equals(this.furniture)) {
            this.furniture = furniture;
            this.selectedFurniture = null; // only reset on location change
        }
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

        List<FurnitureAction> actions = selectedFurniture.getActions();
        if (actions.isEmpty()) {
            lines.add("No actions available.");
        } else {
            for (int i = 0; i < actions.size(); i++) {
                FurnitureAction action = actions.get(i);
                lines.add((i + 1) + ". " + action.getName());

                // --- Needs effects ---
                Map<String, Double> needs = action.affectedNeedsByActionMap();
                if (!needs.isEmpty()) {
                    StringBuilder sb = new StringBuilder("   Needs: ");
                    boolean first = true;
                    for (Map.Entry<String, Double> entry : needs.entrySet()) {
                        if (!first) {
                            sb.append(", ");
                        }
                        sb.append(entry.getKey())
                                .append(" ")
                                .append(formatEffect(entry.getValue()));
                        first = false;
                    }
                    lines.add(sb.toString());
                }

                // --- Skills effects ---
                Map<String, Double> skills = action.affectedSkillsByActionMap();
                if (!skills.isEmpty()) {
                    StringBuilder sb = new StringBuilder("   Skills: ");
                    boolean first = true;
                    for (Map.Entry<String, Double> entry : skills.entrySet()) {
                        if (!first) {
                            sb.append(", ");
                        }
                        sb.append(entry.getKey())
                                .append(" ")
                                .append(formatEffect(entry.getValue()));
                        first = false;
                    }
                    lines.add(sb.toString());
                }

                // --- Cost & time ---
                double cost = action.moneyDeducted();
                double time = action.getTimeRequired();
                StringBuilder meta = new StringBuilder("   ");
                if (cost > 0) {
                    meta.append("Cost: $").append(String.format("%.2f", cost));
                    if (time > 0) {
                        meta.append("  ");
                    }
                }
                if (time > 0) {
                    meta.append("Time: ").append(formatTime(time));
                }
                if (meta.toString().trim().length() > 0) {
                    lines.add(meta.toString());
                }

                lines.add(""); // blank line between actions for readability
            }
        }

        lines.add("0. Back");
        return lines;
    }

    /**
     * Formats a numeric effect with a leading + or - sign.
     */
    private String formatEffect(double value) {
        return (value >= 0 ? "+" : "") + String.format("%.0f", value);
    }

    /**
     * Converts fractional hours to a readable string, e.g. 0.5 -> "30 min", 1.5
     * -> "1h 30min".
     */
    private String formatTime(double hours) {
        int totalMinutes = (int) Math.round(hours * 60);
        int h = totalMinutes / 60;
        int m = totalMinutes % 60;
        if (h == 0) {
            return m + " min";
        }
        if (m == 0) {
            return h + "h";
        }
        return h + "h " + m + "min";
    }
}
