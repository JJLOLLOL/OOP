package ui;

import core.GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameLayout {
    private static final int LEFT = 30;
    private static final int RIGHT = 46;
    public Scanner scanner;
    public GameLayout() {
        this.scanner = new Scanner(System.in);
    }

//    Need to store attributes in memory of game engine
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

//    Options are static for now, eventually its based on activities at Home
    public List<String> setOptions(ArrayList<String> options) {
        List<String> optionsList = new ArrayList<>();
        optionsList.add(Colour.GRAY + "OPTIONS" + Colour.RESET);
        for (int i = 0; i < options.size(); i++) {
            optionsList.add("[" + (i + 1) + "] " + options.get(i));
        }
        return optionsList;
    }

    public void render(GameEngine engine) {
        ArrayList<String> options = new ArrayList<>();
        options.add("Eat");
        options.add("Sleep");
        options.add("Quit Game");
        List<String> right = this.setOptions(options);
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
        System.out.println("│ " + pad(left, LEFT - 1) + "│ " + pad(right, RIGHT - 1) + "│");
    }

    private static String pad(String text, int width) {
        int visible = text.replaceAll("\\u001B\\[[;\\d]*m", "").length();
        int spaces = width - visible;
        if (spaces < 0) {
            spaces = 0;
        }
        return text + " ".repeat(spaces);
    }

    public static String stat(String name, int value) {
        return name + " [" + ProgressBar.bar(value) + "]";
    }
}
