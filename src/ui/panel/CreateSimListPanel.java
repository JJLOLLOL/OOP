package ui.panel;

import java.util.ArrayList;
import java.util.List;

/**
 * LEFT panel — displays the committed sim list only. Stateless display: driven
 * entirely by the data passed in via setters.
 */
public class CreateSimListPanel implements Panel {

    private final List<String[]> sims = new ArrayList<>();
    private int selectedSim = -1;

    public void setSims(List<String[]> sims) {
        this.sims.clear();
        this.sims.addAll(sims);
    }

    /**
     * Highlights a row; pass -1 to clear.
     */
    public void setSelectedSim(int index) {
        this.selectedSim = index;
    }

    public void reset() {
        sims.clear();
        selectedSim = -1;
    }

    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();

        
        if (!sims.isEmpty()) {
            lines.add("Sim List");
            lines.add("");
        }

        for (int i = 0; i < sims.size(); i++) {
            String[] sim = sims.get(i);
            boolean isSelected = (i == selectedSim);

            lines.add((isSelected ? "> " : "  ") + "Sim " + (i + 1)
                    + (isSelected ? " [selected]" : ""));
            lines.add("  Name:   " + sim[0]);
            lines.add("  Age:    " + sim[1]);
            lines.add("  Gender: " + sim[2]);
            lines.add("");
        }

        return lines;
    }
}
