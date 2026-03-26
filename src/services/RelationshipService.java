package services;

import Types.InteractionList;
import Types.RelationshipList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.character.Character;
import models.character.NPCCharacter;
import models.character.Relationship;
import models.character.SimCharacter;

public class RelationshipService {

    private final Map<Character, Map<Character, Relationship>> relationships = new HashMap<>();

    public void registerNewSim(SimCharacter newSim, List<SimCharacter> sims, List<NPCCharacter> npcs) {
        link(newSim, npcs);
        for (SimCharacter sim : sims) {
            if (sim != newSim) {
                link(newSim, sim);
            }
        }
    }

    public String interact(Character from, Character to, InteractionList type) {
        Relationship r = getOrCreate(from, to);
        r.adjust(type.getEffect());
        return from.getName() + " " + type.getLabel() + " " + to.getName() + "\n"
                + to.getName() + type.getReaction() + "\n"
                + "Relationship with " + to.getName() + " "
                + (type.getEffect() > 0 ? "improved" : type.getEffect() < 0 ? "worsened" : "unchanged")
                + " to " + r.getScore();
    }

    public int getScore(Character from, Character to) {
        Map<Character, Relationship> map = relationships.get(from);
        if (map == null) {
            return 0;
        }
        Relationship r = map.get(to);
        return r == null ? 0 : r.getScore();
    }

    public RelationshipList getStatus(Character from, Character to) {
        Map<Character, Relationship> map = relationships.get(from);
        if (map == null) {
            return RelationshipList.ACQUAINTANCE;
        }
        Relationship r = map.get(to);
        return r == null ? RelationshipList.ACQUAINTANCE : r.getStatus();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private Relationship getOrCreate(Character from, Character to) {
        return relationships
                .computeIfAbsent(from, k -> new HashMap<>())
                .computeIfAbsent(to, k -> new Relationship());
    }

    private void link(Character a, Character b) {
        Relationship r = new Relationship();
        relationships.computeIfAbsent(a, k -> new HashMap<>()).put(b, r);
        relationships.computeIfAbsent(b, k -> new HashMap<>()).put(a, r);
    }

    private void link(Character a, List<? extends Character> others) {
        for (Character other : others) {
            link(a, other);
        }
    }
}
