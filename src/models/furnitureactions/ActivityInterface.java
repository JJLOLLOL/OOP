package models.furnitureactions;

import java.util.Map;
import models.SimCharacter;

public interface ActivityInterface {

    String getName();

    String getDescription();

    double getTimeRequired();

    // Returns a map of needs affected by the action, where the key is the need name and the 
    // value is the amount it changes (positive for increase, negative for decrease).
    Map<String, Double> affectedNeedsByActionMap();

    // Returns a map of skills affected by the action, where the key is the skill name and 
    // the value is the amount it changes (positive for increase, negative for decrease).
    Map<String, Double> affectedSkillsByActionMap();

    double moneyDeducted();

    boolean perform(SimCharacter character);
}
