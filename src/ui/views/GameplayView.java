package ui.views;

import core.GameState;
import controller.PlayController;
import core.WorldRegistry;
import models.character.SimCharacter;
import models.location.Location;
import ui.panels.ActionsPanelView;
import ui.panels.NotificationsPanelView;
import ui.panels.SkillsPanelView;
import ui.panels.StatsPanelView;

import java.util.List;

import static ui.ConsoleUtils.*;
import static ui.Renderer.*;

/**
 * Renders the user interface for the main gameplay phase.
 */
public class GameplayView {

    /**
     * Renders the main gameplay screen as a four-panel bordered box.
     *
     * @param state the current {@link GameState}
     * @param world the {@link WorldRegistry}
     * @param playController the {@link PlayController}
     */
    public static void render(GameState state, WorldRegistry world, PlayController playController) {
        SimCharacter player = state.getActivePlayer();
        Location loc = player.getLocation();
        PlayController.Step step = playController.getActiveHandler().getStep();

        List<String> stats = StatsPanelView.build(player, loc, state, world);
        List<String> actions = ActionsPanelView.build(step, loc, player, state, world, playController);
        List<String> skills = SkillsPanelView.build(player);
        List<String> notifs = NotificationsPanelView.build(player);

        LEFT_W = Math.max(MIN_COL_W, maxVisible(stats));
        MID_W = Math.max(MIN_COL_W, maxVisible(actions));
        SKILLS_W = Math.max(MIN_COL_W, maxVisible(skills));
        NOTIF_W = Math.max(MIN_COL_W, maxVisible(notifs));
        INNER_W = (LEFT_W + 2) + (MID_W + 2) + (SKILLS_W + 2) + (NOTIF_W + 2) + 3;

        printBoxTop(CLOCK + "DAY " + state.getGameClock().getDays() + "  ─  "
                + String.format("%02d:%02d", state.getGameClock().getHours(), state.getGameClock().getMinutes()) + RESET);

        System.out.println(BORDER + "├" + seg(LEFT_W + 2) + "┬" + seg(MID_W + 2) + "┬" + seg(SKILLS_W + 2)
                + "┬" + seg(NOTIF_W + 2) + "┤" + RESET);

        int rows = Math.max(Math.max(stats.size(), actions.size()), Math.max(skills.size(), notifs.size()));
        for (int i = 0; i < rows; i++) {
            System.out.println(
                    BORDER + "│" + RESET + " " + padColoured(get(stats, i), LEFT_W)
                            + " " + BORDER + "│" + RESET + " " + padColoured(get(actions, i), MID_W)
                            + " " + BORDER + "│" + RESET + " " + padColoured(get(skills, i), SKILLS_W)
                            + " " + BORDER + "│" + RESET + " " + padColoured(get(notifs, i), NOTIF_W)
                            + " " + BORDER + "│" + RESET);
        }

        System.out.println(BORDER + "└" + seg(LEFT_W + 2) + "┴" + seg(MID_W + 2) + "┴" + seg(SKILLS_W + 2)
                + "┴" + seg(NOTIF_W + 2) + "┘" + RESET);
        System.out.print("\n> ");
    }

    /**
     * Safely returns the indexed row from a panel list, or an empty string when
     * the list is shorter than the requested row.
     */
    private static String get(List<String> list, int i) {
        return i < list.size() ? list.get(i) : "";
    }

    /**
     * Renders the top border row and centered clock heading for the gameplay
     * box layout.
     */
    private static void printBoxTop(String clock) {
        System.out.println(BORDER + "┌" + seg(INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + centerColoured(clock, INNER_W) + BORDER + "│" + RESET);
    }
}
