package ui;

public class ConsoleUI {

    private static final StringBuilder buf = new StringBuilder();

    public static void clear() {
        buf.append("\033[2J\033[3J\033[H");
    }

    public static void moveCursor(int row, int col) {
        buf.append(String.format("\033[%d;%dH", row, col));
    }

    public static void print(String text) {
        buf.append(text);
    }

    public static void println(String text) {
        buf.append(text).append("\n");
    }

    /**
     * Call once at the end of a full frame to push everything to the terminal
     */
    public static void flush() {
        if (buf.length() == 0)
            return;
        System.out.print(buf);
        System.out.flush();
        buf.setLength(0);
    }
}
