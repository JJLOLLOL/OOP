package services;

import java.util.List;
import models.character.Character;
import models.character.NPCCharacter;
import models.character.SimCharacter;
import types.InteractionList;

public class RelationshipService {

    public void registerNewSim(SimCharacter newSim, List<SimCharacter> sims, List<NPCCharacter> npcs) {
        for (NPCCharacter npc : npcs) {
            newSim.initializeRelationshipWith(npc);
        }

        for (SimCharacter sim : sims) {
            if (sim != newSim) {
                newSim.initializeRelationshipWith(sim);
            }
        }
    }


    public String interact(Character from, Character to, InteractionList type) {
        from.changeRelationshipWith(to, type.getEffect());

        int score = from.getRelationshipScoreWith(to);

        return from.getName() + " " + type.getLabel() + " " + to.getName() + "\n"
                + to.getName() + type.getReaction() + "\n"
                + "Relationship with " + to.getName() + " "
                + (type.getEffect() > 0 ? "improved" : type.getEffect() < 0 ? "worsened" : "unchanged")
                + " to " + score;
    }
}
