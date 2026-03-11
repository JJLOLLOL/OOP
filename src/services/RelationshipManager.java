package services;

import java.util.HashMap;
import java.util.Map;

import models.Character;
import models.Relationship;

import Types.InteractionType;

public class RelationshipManager {
  private Map<Character, Map<Character, Relationship>> relationships = new HashMap<>();

  @FunctionalInterface
  public interface RelationshipVisitor {
    void visit(Character from, Character to, Relationship relationship);
  }
  
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

  /**
   * forEachRelationship() - Iterates through ALL relationships in the system.
   * 
   * This is the PRIMARY WAY GameEngine should apply custom business logic
   * (bonuses, penalties, events) without hardcoding them into RelationshipManager.
   * 
   * CRITICAL DESIGN PRINCIPLE:
   * - RelationshipManager tracks relationships
   * - GameEngine applies rules/logic using this method
   * - This separation keeps concerns separate and code flexible
   * 
   * Usage Example in GameEngine:
   * ┌─────────────────────────────────────────────────────┐
   * │ manager.forEachRelationship((from, to, rel) -> {    │
   * │   // Example 1: Rich male bonus                     │
   * │   if (from.getMoney() > 5000 && from.isMale()) {    │
   * │     rel.changeScore(200);                           │
   * │   }                                                  │
   * │   // Example 2: Best friend skill sharing            │
   * │   if (rel.getScore() >= 90) {                       │
   * │     from.getSkills().shareWith(to.getSkills());     │
   * │   }                                                  │
   * │ });                                                  │
   * └─────────────────────────────────────────────────────┘
   * 
   * @param visitor Functional interface implementing custom relationship logic
   */
  // Lets GameEngine apply custom rules (bonuses, quests, events) without hardcoding
  // them into RelationshipManager.
  public void forEachRelationship(RelationshipVisitor visitor) {
    for (Map.Entry<Character, Map<Character, Relationship>> fromEntry : relationships.entrySet()) {
      Character from = fromEntry.getKey();
      for (Map.Entry<Character, Relationship> toEntry : fromEntry.getValue().entrySet()) {
        visitor.visit(from, toEntry.getKey(), toEntry.getValue());
      }
    }
  }

  /**
   * getRelationshipsOf() - Returns all relationships for a specific character.
   * 
   * Useful for:
   * - Displaying character's current relationships to player
   * - Finding all people character has a relationship with
   * - Checking if character has relationship with someone
   * 
   * @param from Character to get relationships for
   * @return Map of (Character -> Relationship) for that character
   */
  public Map<Character, Relationship> getRelationshipsOf(Character from) {
    return relationships.getOrDefault(from, new HashMap<>());
  }
}