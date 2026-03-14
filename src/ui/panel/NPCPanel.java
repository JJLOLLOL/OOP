package ui.panel;

import java.util.ArrayList;
import java.util.List;

import models.Character;
import models.NPCCharacter;
import services.RelationshipManager;

public class NPCPanel implements panel {

  private String locationName;
  private List<NPCCharacter> npcs;
  private Character player;
  private RelationshipManager relationshipManager;

  public NPCPanel(String locationName,
                  List<NPCCharacter> npcs,
                  Character player,
                  RelationshipManager relationshipManager) {

    this.locationName = locationName;
    this.npcs = npcs;
    this.player = player;
    this.relationshipManager = relationshipManager;
  }

  @Override
  public List<String> render() {

    List<String> lines = new ArrayList<>();

    lines.add("You arrived at: " + locationName);
    lines.add("");

    if (npcs == null || npcs.isEmpty()) {
      lines.add("No one is around right now.");
      return lines;
    }

    for (NPCCharacter npc : npcs) {

      int score = relationshipManager.getScore(player, npc);
      String status = relationshipManager.getStatus(player, npc);

      lines.add("You see " + npc.getName());

      if (npc.getDescription() != null) {
        lines.add("  Description : " + npc.getDescription());
      }

      lines.add("  Relationship: " + status + " (" + score + ")");
      lines.add("");
    }

    return lines;
  }
}