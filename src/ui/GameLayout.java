package ui;

import core.GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameLayout {
    private static final int CELL_WIDTH = 30;
    private static final int CONTENT_ROWS = 16;
    private static final int frameStartRow = 3;

    public static void drawFrame(String leftTitle, String rightTitle) {
        clearScreen();
        String lTitle = center(leftTitle, CELL_WIDTH);
        String rTitle = center(rightTitle, CELL_WIDTH);

        // Top border + title row + divider
        System.out.println(Colour.CYAN + "┌" + "─".repeat(CELL_WIDTH + 2) + "┬" + "─".repeat(CELL_WIDTH + 2) + "┐" + Colour.RESET);
        System.out.println(Colour.CYAN + "│ " + Colour.RESET + Colour.BOLD + Colour.WHITE + lTitle + Colour.RESET + Colour.CYAN + " │ " + Colour.RESET + Colour.BOLD + Colour.WHITE + rTitle + Colour.RESET + Colour.CYAN + " │" + Colour.RESET);
        System.out.println(Colour.CYAN + "├" + "─".repeat(CELL_WIDTH + 2) + "┼" + "─".repeat(CELL_WIDTH + 2) + "┤" + Colour.RESET);

        // Empty content rows
        for (int i = 0; i < CONTENT_ROWS; i++) {
            System.out.println(Colour.CYAN + "│ " + Colour.RESET + " ".repeat(CELL_WIDTH) + Colour.CYAN + " │ " + Colour.RESET + " ".repeat(CELL_WIDTH) + Colour.CYAN + " │" + Colour.RESET);
        }

        // Bottom border + status box
        System.out.println(Colour.CYAN + "└" + "─".repeat(CELL_WIDTH + 2) + "┴" + "─".repeat(CELL_WIDTH + 2) + "┘" + Colour.RESET);
        System.out.println(Colour.CYAN + "┌" + "─".repeat((CELL_WIDTH + 2) * 2 + 1) + "┐" + Colour.RESET);
        System.out.println(Colour.CYAN + "│ " + Colour.RESET + " ".repeat((CELL_WIDTH + 2) * 2 - 1) + Colour.CYAN + " │" + Colour.RESET);
        System.out.println(Colour.CYAN + "└" + "─".repeat((CELL_WIDTH + 2) * 2 + 1) + "┘" + Colour.RESET);
        System.out.print(Colour.CYAN + " ❯ " + Colour.RESET);
        System.out.flush();
    }

    public static void updateLeftCell(List<String> lines) {
        updateCell(lines, 0);
    }

    public static void updateRightCell(List<String> lines) {
        updateCell(lines, 1);
    }

    public static void updateStatusBar(String message) {
        int statusRow = frameStartRow + CONTENT_ROWS + 3;
        System.out.print("\033[s");
        moveCursor(statusRow, 2);
        String content = padOrTruncate(message, (CELL_WIDTH + 2) * 2 - 1);
        System.out.print(Colour.YELLOW + content + Colour.RESET);
        System.out.print("\033[u");
        System.out.flush();
    }

    private static void updateCell(List<String> lines, int cellIndex) {
        int col = (cellIndex == 0) ? 2 : (CELL_WIDTH + 4);
        System.out.print("\033[s");
        for (int row = 0; row < CONTENT_ROWS; row++) {
            moveCursor(frameStartRow + 1 + row, col);
            String content  = (row < lines.size()) ? lines.get(row) : "";
            int    padding  = Math.max(0, CELL_WIDTH - stripAnsi(content).length());
            System.out.print(content + " ".repeat(padding));
        }
        System.out.print("\033[u");
        System.out.flush();
    }
    
    public static String statBar(String label, double value, int barLen) {
        int filled = Math.max(0, Math.min(barLen, (int) Math.round((value / 100.0) * barLen)));
        String colour = value >= 60 ? Colour.GREEN : value >= 30 ? Colour.YELLOW : Colour.RED;
        String bar    = "█".repeat(filled) + "░".repeat(barLen - filled);
        return String.format("%s%-8s %s%s%s %3.0f%%", Colour.WHITE, label, colour, bar, Colour.RESET, value);
    }

    public static String center(String s, int width) {
        int pad = Math.max(0, width - s.length());
        return " ".repeat(pad / 2) + s + " ".repeat(pad - pad / 2);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void moveCursor(int row, int col)  {
        System.out.print("\033[" + row + ";" + col + "H");
    }

    public static String stripAnsi(String s) {
        return s.replaceAll("\033\\[[;\\d]*m", "");
    }

    public static String padOrTruncate(String s, int width) {
        String stripped = stripAnsi(s);
        if (stripped.length() > width) return s.substring(0, width);
        return s + " ".repeat(width - stripped.length());
    }


}
