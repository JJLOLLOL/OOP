package ui;

import java.util.List;

public class GameLayout {

    private static final int CELL_WIDTH   = 38;
    private static final int CONTENT_ROWS = 16;

    // ── Frame ─────────────────────────────────────────────────────────────

    public static void drawFrame(String leftTitle, String rightTitle) {
        clearScreen();
        String lTitle = center(leftTitle, CELL_WIDTH);
        String rTitle = center(rightTitle, CELL_WIDTH);

        printDivider("┌", "┬", "┐");
        System.out.println("│ " + lTitle + " │ " + rTitle + " │");
        printDivider("├", "┼", "┤");
    }

    // ── Cell updates ──────────────────────────────────────────────────────

    /**
     * Prints both cells side by side from their line lists.
     * Call this instead of updateLeftCell/updateRightCell separately.
     */
    public static void drawCells(List<String> left, List<String> right) {
        int rows = Math.max(left.size(), right.size());
        rows     = Math.max(rows, CONTENT_ROWS);

        for (int i = 0; i < rows; i++) {
            String l = (i < left.size())  ? left.get(i)  : "";
            String r = (i < right.size()) ? right.get(i) : "";
            System.out.println("│ " + padOrTruncate(l, CELL_WIDTH) + " │ " + padOrTruncate(r, CELL_WIDTH) + " │");
        }

        printDivider("└", "┴", "┘");
    }

    // ── Status bar ────────────────────────────────────────────────────────

    public static void drawStatusBar(String message) {
        int width = (CELL_WIDTH + 2) * 2 + 1;
        printSingleDivider("┌", "┐", width);
        System.out.println("│ " + padOrTruncate(message, width - 2) + " │");
        printSingleDivider("└", "┘", width);
        System.out.print(" ❯ ");
        System.out.flush();
    }

    // ── Stat bar ──────────────────────────────────────────────────────────

    public static String statBar(String label, double value, int barLen) {
        int filled = Math.max(0, Math.min(barLen, (int) Math.round((value / 100.0) * barLen)));
        String bar = "█".repeat(filled) + "░".repeat(barLen - filled);
        return String.format("%-8s %s %3.0f%%", label, bar, value);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static void printDivider(String left, String mid, String right) {
        System.out.println(left + "─".repeat(CELL_WIDTH + 2) + mid + "─".repeat(CELL_WIDTH + 2) + right);
    }

    private static void printSingleDivider(String left, String right, int width) {
        System.out.println(left + "─".repeat(width) + right);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String center(String s, int width) {
        int pad = Math.max(0, width - s.length());
        return " ".repeat(pad / 2) + s + " ".repeat(pad - pad / 2);
    }

    public static String padOrTruncate(String s, int width) {
        if (s.length() > width) return s.substring(0, width);
        return s + " ".repeat(width - s.length());
    }
}