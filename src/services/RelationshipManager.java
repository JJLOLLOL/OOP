package services;

import java.util.HashMap;
import java.util.Map;

import models.Character;
import models.Relationship;

import Types.InteractionType;

public class RelationshipManager {
  private Map<Character, Map<Character, Relationship>> relationships = new HashMap<>();
  
  // register new character
  public void register(Character c) {
    relationships.putIfAbsent(c, new HashMap<>());
  }
  
  public String updateStatus(Character from, Character to, InteractionType type) {
    register(from);
    register(to);
    return relationships
        .computeIfAbsent(from, k -> new HashMap<>())
        .computeIfAbsent(to, k -> new Relationship(0))
        .applyInteraction(type, from.getName(), to.getName());
  }
  
  public void updateStatus(Character from, Character to, int value) {
    register(from);
    register(to);
    relationships
        .computeIfAbsent(from, k -> new HashMap<>())
        .computeIfAbsent(to, k -> new Relationship(0))
        .changeScore(value);
  }

  public String interact(Character from, Character to, InteractionType interaction) {
    return updateStatus(from, to, interaction);
  }

  public int getScore(Character from, Character to) {
    if (!relationships.containsKey(from))
      return 0;
    Relationship r = relationships.get(from).get(to);
    return r == null ? 0 : r.getScore();
  }
  
  public String getStatus(Character from, Character to) {
    if (!relationships.containsKey(from))
      return "Stranger";
    Relationship r = relationships.get(from).get(to);
    return r == null ? "Stranger" : r.getStatus();
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
}