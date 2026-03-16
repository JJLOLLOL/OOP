package ui.panel;

import java.util.ArrayList;
import java.util.List;
import models.Character;
import models.NPCCharacter;
import models.SimCharacter;
import services.RelationshipManager;

public class NearbyPanel implements Panel {

    private String locationName;
    private List<SimCharacter> nearbySims = new ArrayList<>();
    private List<NPCCharacter> nearbyNpcs = new ArrayList<>();
    private Character player;
    private RelationshipManager relationshipManager;

    public void setNearby(String locationName,
            List<SimCharacter> nearbySims,
            List<NPCCharacter> nearbyNpcs,
            Character player,
            RelationshipManager relationshipManager) {
        this.locationName = locationName;
        this.nearbySims = nearbySims;
        this.nearbyNpcs = nearbyNpcs;
        this.player = player;
        this.relationshipManager = relationshipManager;
    }

    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();

        lines.add("You are at " + locationName);
        lines.add("");

        boolean anyoneHere = !nearbySims.isEmpty() || !nearbyNpcs.isEmpty();
        if (!anyoneHere) {
            lines.add("No one is around right now.");
            return lines;
        }

        // available sims
        for (SimCharacter sim : nearbySims) {
            int score = relationshipManager.getScore(player, sim);
            String status = relationshipManager.getStatus(player, sim);
            lines.add("You see " + sim.getName() + " [Sim]");
            lines.add("  " + status + " (" + score + ")");
            lines.add("");
        }

        // available npcs
        for (NPCCharacter npc : nearbyNpcs) {
            int score = relationshipManager.getScore(player, npc);
            String status = relationshipManager.getStatus(player, npc);
            lines.add("You see " + npc.getName());
            if (npc.getDescription() != null && !npc.getDescription().isBlank()) {
                lines.add("  " + npc.getDescription());
            }
            lines.add("  " + status + " (" + score + ")");
            lines.add("");
        }
        return lines;
    }
}
