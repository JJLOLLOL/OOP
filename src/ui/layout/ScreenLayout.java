package ui.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import ui.ConsoleUI;
import ui.panel.Panel;

public class ScreenLayout {

    private static final int CELL_WIDTH = 60;
    private static final int CONTENT_ROWS = 30;
    private static final int FRAME_WIDTH = (CELL_WIDTH + 2) * 2 + 1;
    private static final int TOP_ROW = 2;
    private static final int LEFT_COL = 3;
    private final FrameType frameType;
    private final Map<Region, Panel> panels = new HashMap<>();
    private int inputRow;
    private InputMode inputMode = InputMode.REQUEST;
    private static final String ANSI_REGEX = "\u001B\\[[;\\d]*m";
    private String errorMessage = null;

    public ScreenLayout(FrameType frameType) {
        this.frameType = frameType;
    }

    public void setPanel(Region region, Panel panel) {
        if (!frameType.supports(region)) {
            throw new IllegalArgumentException("Region " + region + " not supported by " + frameType);
        }
        panels.put(region, panel);
    }
    public enum InputMode {
        REQUEST,
        CONFIRM,
    }

    public void render(String title) {
        ConsoleUI.clear();
        drawFrame(title);
        switch (frameType) {
            case SINGLE -> drawSingle(panels.get(Region.MAIN).render());
            case DOUBLE_VERTICAL -> drawDoubleVertical(panels.get(Region.TOP).render(), panels.get(Region.BOTTOM).render());
            case DOUBLE_HORIZONTAL -> drawDoubleHorizontal(panels.get(Region.LEFT).render(), panels.get(Region.RIGHT).render());
            case QUAD -> drawQuad(panels.get(Region.TOP_LEFT).render(), panels.get(Region.TOP_RIGHT).render(), panels.get(Region.BOTTOM_LEFT).render(), panels.get(Region.BOTTOM_RIGHT).render());
        }
        drawInputPrompt(inputMode, errorMessage);
    }

    public String readField(String label, Scanner scanner) {
        ConsoleUI.moveCursor(inputRow, LEFT_COL + 2);
        System.out.print(pad("", FRAME_WIDTH - 2));
        ConsoleUI.moveCursor(inputRow, LEFT_COL + 2);
        if (inputMode == InputMode.CONFIRM) {
            System.out.println("> ");
            ConsoleUI.moveCursor(inputRow, LEFT_COL + 4);
        }
        else {
            System.out.print(label + ": ");
            ConsoleUI.moveCursor(inputRow, LEFT_COL + 2 + label.length() + 2);
        }
        return scanner.nextLine();
    }

    public Boolean readConfirm(Scanner scanner) {
        while (true) {
            ConsoleUI.moveCursor(inputRow, LEFT_COL + 2);
            System.out.println("> ");
            ConsoleUI.moveCursor(inputRow, LEFT_COL + 4);
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "y", "yes", "1", "true" -> {
                    return true;
                }
                case "n", "no", "0", "false" -> {
                    return false;
                }
            }
        }
    }

    private void drawFrame(String title) {
        put(TOP_ROW, "┌" + "─".repeat(FRAME_WIDTH) + "┐");
        put(TOP_ROW + 1, "│ " + center(title, FRAME_WIDTH - 2) + " │");
        put(TOP_ROW + 2, "├" + "─".repeat(FRAME_WIDTH) + "┤");
    }

    private void drawSingle(List<String> lines) {
        for (int i = 0; i < CONTENT_ROWS; i++) {
            String line = i < lines.size() ? lines.get(i) : "";
            put(TOP_ROW + 3 + i, "│ " + pad(line, FRAME_WIDTH - 2) + " │");
        }
    }

    private void drawDoubleVertical(List<String> top, List<String> bottom) {
        int halfRows = CONTENT_ROWS / 2;
        for (int i = 0; i < halfRows; i++) {
            String line = i < top.size() ? top.get(i) : "";
            put(TOP_ROW + 3 + i, "│ " + pad(line, FRAME_WIDTH - 2) + " │");
        }
        put(TOP_ROW + 3 + halfRows, "├" + "─".repeat(FRAME_WIDTH) + "┤");
        for (int i = 0; i < halfRows; i++) {
            String line = i < bottom.size() ? bottom.get(i) : "";
            put(TOP_ROW + 4 + halfRows + i, "│ " + pad(line, FRAME_WIDTH - 2) + " │");
        }
    }
    private void drawDoubleHorizontal(List<String> left, List<String> right) {
        for (int i = 0; i < CONTENT_ROWS; i++) {
            String l = i < left.size() ? left.get(i) : "";
            String r = i < right.size() ? right.get(i) : "";
            int row = TOP_ROW + 3 + i;
            put(row, "│ " + pad(l, CELL_WIDTH) + " │ " + pad(r, CELL_WIDTH) + " │");
        }
    }

    private void drawQuad(List<String> tl, List<String> tr, List<String> bl, List<String> br) {
        int halfRows = CONTENT_ROWS / 2;
        for (int i = 0; i < halfRows; i++) {
            String left = i < tl.size() ? tl.get(i) : "";
            String right = i < tr.size() ? tr.get(i) : "";
            put(TOP_ROW + 3 + i, "│ " + pad(left, CELL_WIDTH) + " │ " + pad(right, CELL_WIDTH) + " │");
        }
        put(TOP_ROW + 3 + halfRows, "├" + "─".repeat(CELL_WIDTH + 2) + "┼" + "─".repeat(CELL_WIDTH + 2) + "┤");
        for (int i = 0; i < halfRows; i++) {
            String left = i < bl.size() ? bl.get(i) : "";
            String right = i < br.size() ? br.get(i) : "";
            put(TOP_ROW + 4 + halfRows + i, "│ " + pad(left, CELL_WIDTH) + " │ " + pad(right, CELL_WIDTH) + " │");
        }
    }

    private void drawInputPrompt(InputMode mode, String error) {
        int footerTop = TOP_ROW + 3 + CONTENT_ROWS;
        put(footerTop, "├" + "─".repeat(FRAME_WIDTH) + "┤");
        String text;
        if (error != null)
            text = "Error: " + error;
        else if (mode == InputMode.CONFIRM)
            text = "Confirm? (y/n)";
        else
            text = "Input the following field";
        put(footerTop + 1, "│ " + pad(text, FRAME_WIDTH - 2) + " │");
        put(footerTop + 2, "│ " + pad("", FRAME_WIDTH - 2) + " │");
        put(footerTop + 3, "└" + "─".repeat(FRAME_WIDTH) + "┘");
        inputRow = footerTop + 2;
    }


    public void setInputMode(InputMode mode) {
        this.inputMode = mode;
    }

    public void setErrorMessage(String error) {
        this.errorMessage = error;
    }

    private void put(int row, String text) {
        ConsoleUI.moveCursor(row, LEFT_COL);
        System.out.print(text);
    }
    private String center(String s, int width) {
        int pad = Math.max(0, width - visibleLength(s));
        return " ".repeat(pad / 2) + s + " ".repeat(pad - pad / 2);
    }
    private String stripAnsi(String s) {
        return s.replaceAll(ANSI_REGEX, "");
    }

    private int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    private String pad(String s, int width) {

        int visible = visibleLength(s);
        if (visible >= width) {
            return s;
        }
        return s + " ".repeat(width - visible);
    }
}
