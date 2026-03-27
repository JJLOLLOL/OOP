package services;

import java.util.List;
import models.character.Character;
import models.character.NPCCharacter;
import models.character.SimCharacter;
import types.InteractionType;

/**
 * Coordinates relationship initialization and interaction outcomes between
 * characters.
 */
public class RelationshipService {

    /**
     * Initializes symmetric relationships between a newly created sim and all
     * existing sims and NPCs.
     *
     * @param newSim the newly created sim
     * @param sims the already created playable sims
     * @param npcs all NPCs in the world
     */
    public void registerNewSim(SimCharacter newSim, List<SimCharacter> sims, List<NPCCharacter> npcs) {
        if (newSim == null || sims == null || npcs == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        for (NPCCharacter npc : npcs) {
            newSim.initializeRelationshipWith(npc);
        }

        for (SimCharacter sim : sims) {
            if (sim != newSim) {
                newSim.initializeRelationshipWith(sim);
            }
        }
    }


    /**
     * Applies a social interaction between two characters and returns a
     * descriptive summary of the result.
     *
     * @param from the character initiating the interaction
     * @param to the target character
     * @param type the chosen interaction type
     * @return a player-facing interaction summary
     */
    public String interact(Character from, Character to, InteractionType type) {
        if (from == null || to == null || type == null) {
            throw new IllegalArgumentException("Interaction arguments cannot be null.");
        }
        from.changeRelationshipWith(to, type.getEffect());

        int score = from.getRelationshipScoreWith(to);

        return from.getName() + " " + type.getLabel() + " " + to.getName() + "\n"
                + to.getName() + type.getReaction() + "\n"
                + "Relationship with " + to.getName() + " "
                + (type.getEffect() > 0 ? "improved" : type.getEffect() < 0 ? "worsened" : "unchanged")
                + " to " + score;
    }
}
