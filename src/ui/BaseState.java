package ui;

import core.GameEngine;
import models.SimCharacter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseState implements GameState {

    protected List<String> buildAttributeLines(GameEngine engine) {
        SimCharacter p = engine.getActivePlayer();
        if (p == null) return List.of();

        List<String> lines = new ArrayList<>();
        int barLen = 18;

        lines.add(Colour.CYAN + "  " + p.getName() + "  |  Age " + p.getAge() + "  |  Day " + "TODO" + Colour.RESET);
        lines.add(Colour.GRAY + "  " + "─".repeat(34) + Colour.RESET);
        lines.add("");
        lines.add("  " + GameLayout.statBar("Hunger",  100,  barLen));
        lines.add("  " + GameLayout.statBar("Energy",  100,  barLen));
        lines.add("  " + GameLayout.statBar("Hygiene", 100, barLen));
        lines.add("  " + GameLayout.statBar("Fun",     100,     barLen));
        lines.add("  " + GameLayout.statBar("Social",  100,  barLen));
        lines.add("");
        lines.add(Colour.GRAY + "  " + "─".repeat(34) + Colour.RESET);
        lines.add("  " + Colour.YELLOW  + String.format("Money:  $%.2f", p.getMoney())  + Colour.RESET);
//        lines.add("  " + Colour.MAGENTA + "Mood:   " + p.getMoodLabel()                 + Colour.RESET);
//        lines.add("  " + Colour.WHITE   + "Doing:  " + p.getCurrentActivity()           + Colour.RESET);
        lines.add("");
        if (engine.getCurrentLocation() != null) {
            lines.add("  " + Colour.BLUE
                    + engine.getCurrentLocation().getLocationName() + Colour.RESET);
        }
        return lines;
    }
}
