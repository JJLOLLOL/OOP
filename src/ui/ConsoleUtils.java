package ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for terminal manipulation, ANSI string formatting, and layout calculations.
 */
public class ConsoleUtils {

    // ── ANSI ──────────────────────────────────────────────────────────────────
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String WHITE = "\u001B[37m";
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_MAGENTA = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";

    // ── ANSI / string utilities ───────────────────────────────────────────────
    
    /**
     * Removes all ANSI escape sequences from a string, returning only the visible text.
     */
    public static String stripAnsi(String s) {
        return s == null ? "" : s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    /**
     * Returns the visible (non-ANSI) character length of a string.
     */
    public static int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    /**
     * Returns the maximum visible length across all strings in a list.
     */
    public static int maxVisible(List<String> lines) {
        return lines.stream().mapToInt(ConsoleUtils::visibleLength).max().orElse(0);
    }

    /**
     * Pads an ANSI-formatted string to a given visible width.
     */
    public static String padColoured(String s, int width) {
        if (s == null) {
            s = "";
        }
        String plain = stripAnsi(s);
        if (plain.length() > width) {
            return String.format("%-" + width + "s", plain.substring(0, width));
        }
        return s + " ".repeat(width - plain.length());
    }

    /**
     * Centres an ANSI-formatted string within a given visible width.
     */
    public static String centerColoured(String s, int width) {
        int vlen = visibleLength(s);
        if (vlen >= width) {
            return padColoured(s, width);
        }
        int lpad = (width - vlen) / 2;
        return " ".repeat(lpad) + s + " ".repeat(width - vlen - lpad);
    }

    /**
     * Left-aligns a plain string within a fixed column width, truncating if necessary.
     */
    public static String pad(String s, int width) {
        if (s == null) {
            s = "";
        }
        return String.format("%-" + width + "s", s.length() > width ? s.substring(0, width) : s);
    }

    /**
     * Centres a plain (non-ANSI) string within a fixed column width.
     */
    public static String center(String s, int width) {
        if (s.length() >= width) {
            return pad(s, width);
        }
        int lpad = (width - s.length()) / 2;
        return " ".repeat(lpad) + s + " ".repeat(width - s.length() - lpad);
    }

    /**
     * Wraps a plain string into a list of lines, each no longer than width characters.
     */
    public static List<String> wordWrap(String s, int width) {
        List<String> result = new ArrayList<>();
        while (s.length() > width) {
            int cut = s.lastIndexOf(' ', width);
            if (cut <= 0) {
                cut = width;
            }
            result.add(s.substring(0, cut));
            s = s.substring(cut).stripLeading();
        }
        if (!s.isEmpty()) {
            result.add(s);
        }
        return result;
    }

    /**
     * Formats a duration given in fractional hours as a human-readable string.
     */
    public static String formatHours(double h) {
        if (h < 1.0) {
            return (int) (h * 60) + "min";
        }
        int hrs = (int) h, mins = (int) Math.round((h - hrs) * 60);
        return mins > 0 ? hrs + "h " + mins + "min" : hrs + "h";
    }

    /**
     * Clears the terminal screen using the standard ANSI escape sequence.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Renders a labelled, colour-coded progress bar as a single formatted string.
     *
     * @param name the label displayed to the left of the bar
     * @param nameWidth the column width reserved for the label
     * @param value the current value of the stat being represented
     * @param max the maximum possible value
     * @param colour the ANSI colour code for the filled portion
     * @param suffix additional text appended after the bar
     * @return a single ANSI-formatted string representing the complete bar row
     */
    public static String bar(String name, int nameWidth, int value, int max, String colour, String suffix) {
        int barWidth = 10; // Default bar width from original Renderer
        int filled = Math.min(barWidth, value * barWidth / max);
        int empty = barWidth - filled;
        return BRIGHT_BLACK + String.format("%-" + nameWidth + "s", name) + RESET
                + " " + BRIGHT_BLACK + "[" + RESET + colour + "#".repeat(filled) + RESET
                + BRIGHT_BLACK + "-".repeat(empty) + "]" + RESET
                + " " + colour + suffix + RESET;
    }

    /**
     * Returns a horizontal line segment of {@code n} box-drawing dash
     * characters ({@code ─}).
     *
     * @param n the number of characters in the segment; must be non-negative
     * @return a string of {@code n} repeated {@code ─} characters
     */
    public static String seg(int n) {
        return "─".repeat(n);
    }
}