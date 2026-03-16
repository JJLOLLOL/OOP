package ui.panel;

import java.util.ArrayList;
import java.util.List;
import ui.Color;

public class TemporaryPanel implements Panel {

    private List<String> notifications = new ArrayList<>();

    public void setNotifications(List<String> notifications) {
        this.notifications = new ArrayList<>(notifications);
    }

    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();
        lines.add(Color.YELLOW + "Notifications" + Color.RESET);
        lines.add("─".repeat(40));
        lines.add("");

        if (notifications.isEmpty()) {
            lines.add(Color.DIM + "No new notifications." + Color.RESET);
        } else {
            for (String notif : notifications) {
                List<String> wrapped = wrapText("• " + notif, 56);
                for (String w : wrapped) {
                    lines.add(Color.RED + w + Color.RESET);
                }
                lines.add(""); // spacing between notifications
            }
        }
        return lines;
    }

    private List<String> wrapText(String text, int maxWidth) {
        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() == 0) {
                currentLine.append(word);
            } else if (currentLine.length() + 1 + word.length() <= maxWidth) {
                currentLine.append(" ").append(word);
            } else {
                result.add(currentLine.toString());
                currentLine = new StringBuilder("  ").append(word); // Indent wrapped lines
            }
        }
        if (currentLine.length() > 0) {
            result.add(currentLine.toString());
        }
        return result;
    }
}
