package ui.panels;

import java.util.ArrayList;
import java.util.List;
import models.character.SimCharacter;
import services.NotificationService;

import static ui.ConsoleUtils.*;
import static ui.Renderer.*;

/**
 * Renders the "Notifications" panel of the gameplay UI.
 */
public class NotificationsPanelView {

    /**
     * Builds the notifications panel displaying recent game events for the active Sim.
     *
     * @param player the active {@link SimCharacter} whose notifications are displayed
     * @return an ordered list of ANSI-formatted strings representing the panel rows
     */
    public static List<String> build(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(menuTitle("Notifications"));
        List<String> notes = NotificationService.get(player);
        if (notes.isEmpty()) {
            lines.add(MUTED + "None." + RESET);
            return lines;
        }
        for (String note : notes) {
            String colour = classifyNotification(note);
            for (String seg : note.split("\n")) {
                String clean = seg.trim();
                if (!clean.isEmpty()) {
                    wordWrap(clean, NOTIF_W).forEach(l -> lines.add(colour + l + RESET));
                }
            }
            lines.add("");
        }
        while (!lines.isEmpty() && stripAnsi(lines.get(lines.size() - 1)).isBlank()) {
            lines.remove(lines.size() - 1);
        }
        return lines;
    }

    /**
     * Determines the ANSI colour code for a notification message based on keywords.
     */
    private static String classifyNotification(String note) {
        String l = note.toLowerCase();
        if (l.contains("levelled up") || l.contains("improved") || l.contains("promoted")
                || l.contains("earned") || l.contains("started career")) {
            return BRIGHT_GREEN;
        }
        if (l.contains("failed") || l.contains("starving") || l.contains("exhausted")
                || l.contains("cannot") || l.contains("not enough") || l.contains("worsened")) {
            return BRIGHT_RED;
        }
        if (l.contains("warning") || l.contains("low") || l.contains("lonely")
                || l.contains("bored") || l.contains("dirty") || l.contains("cost")) {
            return BRIGHT_YELLOW;
        }
        return BRIGHT_WHITE;
    }
}