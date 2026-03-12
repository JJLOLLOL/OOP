package ui;

public class ConsoleUI {

    private static int cursorRow = 1;

    public static void resetCursor() {
        System.out.print("\033[H");
        cursorRow = 1;
    }

    public static void clear() {
        System.out.print("\033[2J");
        System.out.print("\033[H");
        System.out.flush();
        cursorRow = 1;
    }

    public static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
        cursorRow = row;
    }

    public static void println(String text) {
        System.out.println(text);
        cursorRow++;
    }

    public static void print(String text) {
        System.out.print(text);
    }

    public static int getCursorRow() {
        return cursorRow;
    }
}
