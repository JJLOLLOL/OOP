package ui.panels;

import java.util.ArrayList;
import java.util.List;
import models.character.SimCharacter;
import models.skill.Skill;

import static ui.ConsoleUtils.*;
import static ui.Renderer.*;

/**
 * Renders the "Skills" panel of the gameplay UI.
 */
public class SkillsPanelView {

    /**
     * Builds the skills panel showing the active Sim's skill levels as progress bars.
     *
     * @param player the active {@link SimCharacter} whose skills are rendered
     * @return an ordered list of ANSI-formatted strings representing the panel rows
     */
    public static List<String> build(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(menuTitle("Skills"));
        for (Skill skill : player.getStats().getSkillViews()) {
            int pct = (int) Math.min(100, (skill.getProgress() / skill.getRequiredXP()) * 100);
            lines.add(bar(skill.getName(), 11, pct, 100,
                    pct >= 70 ? BRIGHT_GREEN : pct >= 40 ? BRIGHT_YELLOW : BRIGHT_BLUE,
                    MUTED + "Lv" + skill.getLevel() + RESET));
        }
        return lines;
    }
}