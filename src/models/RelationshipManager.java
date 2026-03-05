package models;

import java.util.HashMap;
import java.util.Map;

public class RelationshipManager {
  private Map<Character, Map<Character, Relationship>> relationships = new HashMap<>();
  
  // register new character
  public void register(Character c) {
    relationships.putIfAbsent(c, new HashMap<>());
  }
  
  public void updateStatus(Character from, Character to, int value) {
    register(from);
    register(to);
    relationships
      .computeIfAbsent(from, k -> new HashMap<>())
      .computeIfAbsent(to, k -> new Relationship(0))
      .changeScore(value);
  }

  public int getScore(Character from, Character to) {
    if (!relationships.containsKey(from))
      return 0;
    Relationship r = relationships.get(from).get(to);
    return r == null ? 0 : r.getScore();
  }
}