package ui;

import java.util.Arrays;
import java.util.List;

public class GameLayout {
    private static final int LEFT = 30;
    private static final int RIGHT = 46;
    public GameLayout() {}

    List<String> left = Arrays.asList(
        Colour.CYAN + "Location : " + Colour.RESET + "Home",
        Colour.YELLOW + "Money    : " + Colour.RESET + "$0.00",
        Colour.BLUE + "Mood     : " + Colour.RESET + "Good",
        "",
        stat("Hunger ", 100),
        stat("Energy ", 100),
        stat("Hygiene", 100),
        stat("Bladder", 100),
        stat("Social ", 100),
        stat("Fun    ", 100)
    );

//    Options are static for now
    List<String> right = Arrays.asList(
            Colour.GRAY + "OPTIONS" + Colour.RESET,
            "[1] Sleep",
            "[2] Eat",
            "[3] Shower",
            "[4] Toilet",
            "[5] Watch TV",
            "[6] Locations",
            "[7] Change SIM",
            "[8] Exit Game"
    );

    public void render() {
        int rows = Math.max(left.size(), right.size());
        System.out.println("┌" + "─".repeat(LEFT) + "┬" + "─".repeat(RIGHT) + "┐");

        System.out.println("├" + "─".repeat(LEFT) + "┴" + "─".repeat(RIGHT) + "┤");

        for (int i = 0; i < rows; i++) {

            String l = i < left.size() ? left.get(i) : "";
            String r = i < right.size() ? right.get(i) : "";

            row(l, r);
        }

        System.out.println("└" + "─".repeat(LEFT) + "┴" + "─".repeat(RIGHT) + "┘");
    }

    private static void row(String left, String right) {

        System.out.println(
                "│ " + pad(left, LEFT - 1) +
                        "│ " + pad(right, RIGHT - 1) +
                        "│"
        );
    }

    private static String pad(String text, int width) {

        int visible = text.replaceAll("\\u001B\\[[;\\d]*m", "").length();
        int spaces = width - visible;

        if (spaces < 0) spaces = 0;

        return text + " ".repeat(spaces);
    }

    public static String stat(String name, int value) {
        return name + " [" + ProgressBar.bar(value) + "]";
    }
}
