package ui;

public class ProgressBar {
    public static String bar(int percent) {
        int total = 10;
        int filled = percent / 10;
        String bar = "█".repeat(filled) + "░".repeat(total - filled);
        String color;
        if (percent >= 70)
            color = Colour.GREEN;
        else if (percent >= 40)
            color = Colour.YELLOW;
        else
            color = Colour.RED;
        return color + bar + Colour.RESET + " " + percent + "%";
    }
}
