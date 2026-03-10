package services;

import java.util.HashMap;
import models.SimCharacter;
import models.needs.Need;

public class NeedManager {

  private HashMap<String, Need> needs = new HashMap<>();

  public void addNeed(Need need) {
    needs.put(need.getNeedName(), need);
  }

  public Need getNeed(String name) {
    return needs.get(name);
  }

  public void decayAll(SimCharacter character) {
    for (Need need : needs.values()) {
      need.decay();

      if (need.isCriticallyLow()) {
        need.onCriticallyLow(character);
      }
    }
  }
}