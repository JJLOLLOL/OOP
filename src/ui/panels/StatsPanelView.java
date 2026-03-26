package ui.panels;

import Types.RelationshipList;
import core.GameState;
import core.PlayController;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Relation;

import models.career.CareerList;
import models.character.NPCCharacter;
import models.character.SimCharacter;
import models.location.Location;
import models.need.Need;

import static ui.ConsoleUtils.*;
import static ui.Renderer.*;

/**
 * Renders the left "Stats" panel of the gameplay UI.
 */
public class StatsPanelView {

    /**
     * Builds the left stats panel showing the active Sim's core attributes.
     *
     * @param player the active {@link SimCharacter} whose stats are displayed
     * @param loc the {@link Location} the Sim currently occupies
     * @param state the {@link GameState} used to query relationship data
     * @param world the {@link WorldRegistry}
     * @return an ordered list of ANSI-formatted strings representing the panel rows
     */
    public static List<String> build(SimCharacter player, Location loc,
            GameState state, WorldRegistry world) {
        List<String> lines = new ArrayList<>();

        lines.add(SIM_NAME + player.getName() + RESET
                + MUTED + " (" + player.getAge() + player.getGender().charAt(0) + ")" + RESET);

        models.career.Career career = player.getCareer();
        lines.add(career.getCurrentCareer() != CareerList.JOBLESS
                ? BRIGHT_MAGENTA + career.getTitle() + RESET + MUTED + "  " + career.getRank() + RESET
                : MUTED + "Unemployed" + RESET);
        lines.add("");

        for (Need need : player.getStats().getNeedViews()) {
            lines.add(bar(
                    need.getNeedName(), 8, (int) need.getValue(), 100,
                    need.getValue() >= 70 ? BRIGHT_GREEN : need.getValue() >= 40 ? BRIGHT_YELLOW : BRIGHT_RED,
                    String.format("%3d%%", (int) need.getValue())));
        }

        lines.add("");
        lines.add(BRIGHT_YELLOW + "Money: $" + String.format("%.2f", player.getMoney()) + RESET);
        lines.add(BORDER + "─".repeat(LEFT_W) + RESET);
        lines.add(LABEL + "At " + RESET + BRIGHT_CYAN + loc.getLocationName() + RESET);

        List<models.character.Character> chars = PlayController.charsAt(loc, state, world);
        if (chars.isEmpty()) {
            lines.add(MUTED + "No one nearby." + RESET);
        } else {
            lines.add(LABEL + "nearby:" + RESET);
            for (models.character.Character c : chars) {
                RelationshipList status = player.getRelationshipStatus(c);
                int score = player.getRelationshipScoreWith(c);
                String col = score > 0 ? BRIGHT_GREEN : score < 0 ? BRIGHT_RED : BRIGHT_YELLOW;
                lines.add(WHITE + c.getName() + RESET + MUTED + " [" + status.label + "] " + RESET + col + score + RESET);
                if (c instanceof NPCCharacter npc && npc.getDescription() != null && !npc.getDescription().isBlank()) {
                    lines.add(MUTED + "  " + npc.getDescription() + RESET);
                }
            }
        }
        return lines;
    }
}