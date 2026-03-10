package models.furnitureactions;

import java.util.Map;
import models.SimCharacter;

public interface ActivityInterface {

    Map<String, Double> affectedNeeds();

    Map<String, Double> affectedSkills();

    double moneyDeducted();

    boolean perform(SimCharacter character);
}
