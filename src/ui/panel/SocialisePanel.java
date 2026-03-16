package ui.panel;

import Types.InteractionType;
import java.util.ArrayList;
import java.util.List;
import models.NPCCharacter;

public class SocialisePanel implements Panel {

    private List<NPCCharacter> npcs = new ArrayList<>();
    private NPCCharacter selectedNPC = null;

    public void setNPCs(List<NPCCharacter> npcs) {
        this.npcs = npcs;
        this.selectedNPC = null;
    }

    public void selectNPC(NPCCharacter npc) {
        this.selectedNPC = npc;
    }

    public void clearSelection() {
        this.selectedNPC = null;
    }

    @Override
    public List<String> render() {
        return selectedNPC == null ? renderNPCList() : renderInteractionList();
    }

    private List<String> renderNPCList() {
        List<String> lines = new ArrayList<>();
        lines.add("Socialise");
        lines.add("");

        if (npcs.isEmpty()) {
            lines.add("No one around to socialise with.");
        } else {
            for (int i = 0; i < npcs.size(); i++) {
                lines.add((i + 1) + ". " + npcs.get(i).getName());
            }
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }

    private List<String> renderInteractionList() {
        List<String> lines = new ArrayList<>();
        lines.add(selectedNPC.getName());
        lines.add("");

        InteractionType[] types = InteractionType.values();
        for (int i = 0; i < types.length; i++) {
            lines.add((i + 1) + ". " + types[i].getLabel());
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }
}
