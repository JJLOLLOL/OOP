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

/**
 * Manages relationships and interactions between all characters in the game.
 */
public class RelationshipService {

    private final Map<Character, Map<Character, Relationship>> relationships = new HashMap<>();

    /**
     * Registers a new Sim into the relationship tracking system.
     *
     * @param newSim the newly created {@link SimCharacter}
     * @param sims   the list of all existing player Sims
     * @param npcs   the list of all NPCs
     */
    public void registerNewSim(SimCharacter newSim, List<SimCharacter> sims, List<NPCCharacter> npcs) {
        link(newSim, npcs);
        for (SimCharacter sim : sims) {
            if (sim != newSim) {
                link(newSim, sim);
            }
        }
    }

    /**
     * Processes a social interaction between two characters and updates their relationship.
     *
     * @param from the {@link Character} initiating the interaction
     * @param to   the target {@link Character}
     * @param type the {@link InteractionList} type defining the interaction
     * @return a formatted string describing the outcome of the interaction
     */
    public String interact(Character from, Character to, InteractionList type) {
        Relationship r = getOrCreate(from, to);
        r.changeScore(type.getEffect());
        return from.getName() + " " + type.getLabel() + " " + to.getName() + "\n"
                + to.getName() + type.getReaction() + "\n"
                + "Relationship with " + to.getName() + " "
                + (type.getEffect() > 0 ? "improved" : type.getEffect() < 0 ? "worsened" : "unchanged")
                + " to " + r.getScore();
    }

    /**
     * Retrieves the exact numerical relationship score between two characters.
     *
     * @param from the first {@link Character}
     * @param to   the second {@link Character}
     * @return the relationship score
     */
    public int getScore(Character from, Character to) {
        Map<Character, Relationship> map = relationships.get(from);
        if (map == null) {
            return 0;
        }
        Relationship r = map.get(to);
        return r == null ? 0 : r.getScore();
    }

    /**
     * Retrieves the relationship status tier between two characters.
     *
     * @param from the first {@link Character}
     * @param to   the second {@link Character}
     * @return the {@link RelationshipList} status
     */
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
