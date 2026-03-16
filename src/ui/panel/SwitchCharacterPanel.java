package ui.panel;

import java.util.ArrayList;
import java.util.List;
import models.SimCharacter;

public class SwitchCharacterPanel implements Panel {

    private List<SimCharacter> sims = new ArrayList<>();
    private SimCharacter activeSim = null;

    public void setSims(List<SimCharacter> sims, SimCharacter activeSim) {
        this.sims = sims;
        this.activeSim = activeSim;
    }

    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();
        lines.add("Switch Character");
        lines.add("");

        if (sims.isEmpty()) {
            lines.add("No characters available.");
        } else {
            for (int i = 0; i < sims.size(); i++) {
                SimCharacter sim = sims.get(i);
                String suffix = sim.equals(activeSim) ? " (Active)" : "";
                lines.add((i + 1) + ". " + sim.getName() + suffix);
            }
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }
}