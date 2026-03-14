package services;

import Types.InteractionType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Character;
import models.NPCCharacter;
import models.Relationship;
import models.SimCharacter;

public class RelationshipManager {

    private Map<Character, Map<Character, Relationship>> relationships = new HashMap<>();

    @FunctionalInterface
    public interface RelationshipVisitor {

        void visit(Character from, Character to, Relationship relationship);
    }

    public void register(Character c) {
        relationships.putIfAbsent(c, new HashMap<>());
    }

    public void registerNewSim(
            SimCharacter newSim,
            List<SimCharacter> sims,
            List<NPCCharacter> npcs) {

        register(newSim);

        for (NPCCharacter npc : npcs) {
            register(npc);
            Relationship r = new Relationship(0);

            relationships
                    .computeIfAbsent(newSim, k -> new HashMap<>())
                    .put(npc, r);

            relationships
                    .computeIfAbsent(npc, k -> new HashMap<>())
                    .put(newSim, r);
        }

        for (SimCharacter sim : sims) {
            if (sim == newSim) {
                continue;
            }

            register(sim);
            Relationship r = new Relationship(0);

            relationships
                    .computeIfAbsent(newSim, k -> new HashMap<>())
                    .put(sim, r);

            relationships
                    .computeIfAbsent(sim, k -> new HashMap<>())
                    .put(newSim, r);
        }
    }

    public String interact(Character from, Character to, InteractionType interaction) {

        register(from);
        register(to);

        Relationship r = relationships
                .computeIfAbsent(from, k -> new HashMap<>())
                .computeIfAbsent(to, k -> new Relationship(0));

        relationships
                .computeIfAbsent(to, k -> new HashMap<>())
                .putIfAbsent(from, r);

        return r.applyInteraction(interaction, from.getName(), to.getName());
    }

    public int getScore(Character from, Character to) {

        if (!relationships.containsKey(from)) {
            return 0;
        }
        Relationship r = relationships.get(from).get(to);
        return r == null ? 0 : r.getScore();
    }

    public String getStatus(Character from, Character to) {

        if (!relationships.containsKey(from)) {
            return "Stranger";
        }
        Relationship r = relationships.get(from).get(to);
        return r == null ? "Stranger" : r.getStatus();
    }

    public Map<Character, Relationship> getRelationshipsOf(Character from) {

        return relationships.getOrDefault(from, new HashMap<>());
    }

    public void decayRelationships() {

        for (Character from : relationships.keySet()) {
            for (Character to : relationships.get(from).keySet()) {
                Relationship r = relationships.get(from).get(to);
                if (r.getScore() > 0) {
                    r.changeScore(-1);
                }
            }
        }
    }

    public void forEachRelationship(RelationshipVisitor visitor) {

        for (Map.Entry<Character, Map<Character, Relationship>> fromEntry : relationships.entrySet()) {
            Character from = fromEntry.getKey();

            for (Map.Entry<Character, Relationship> toEntry : fromEntry.getValue().entrySet()) {
                visitor.visit(from, toEntry.getKey(), toEntry.getValue());
            }
        }
    }
}
