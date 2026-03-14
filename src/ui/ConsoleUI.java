package ui;

public class ConsoleUI {

    public static void resetCursor() {
        System.out.print("\033[H");
    }

    public static void clear() {
        System.out.print("\033[2J");
        System.out.print("\033[H");
        System.out.flush();
    }

    public static void moveCursor(int row, int col) {
        System.out.printf("\033[%d;%dH", row, col);
    }
}