package ui.panel;

import java.util.ArrayList;
import java.util.List;
import models.Character;
import models.NPCCharacter;
import models.SimCharacter;
import services.RelationshipManager;

/**
 * Shows everyone at the active player's current location: - other
 * player-created sims (SimCharacter) - NPCs (NPCCharacter) Both are displayed
 * with relationship score + status.
 */
public class NearbyPanel implements Panel {

    private String locationName;
    private List<SimCharacter> nearbySims = new ArrayList<>(); // other player sims at this location
    private List<NPCCharacter> nearbyNpcs = new ArrayList<>(); // NPCs at this location
    private Character player;
    private RelationshipManager relationshipManager;

    /**
     * Called every update tick from MainState.
     *
     * @param locationName name of the active player's current location
     * @param nearbySims other SimCharacters at this location (active player
     * excluded)
     * @param nearbyNpcs NPCCharacters at this location
     * @param player the active player (used as relationship subject)
     * @param relationshipManager for score + status lookups
     */
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

        // ── other player sims ─────────────────────────────────────────────────
        for (SimCharacter sim : nearbySims) {
            int score = relationshipManager.getScore(player, sim);
            String status = relationshipManager.getStatus(player, sim);

            lines.add("You see " + sim.getName() + " [Sim]");
            lines.add("  " + status + " (" + score + ")");
            lines.add("");
        }

        // ── NPCs ──────────────────────────────────────────────────────────────
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
