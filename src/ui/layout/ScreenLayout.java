package ui.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ui.ConsoleUI;
import ui.panel.Panel;

public class ScreenLayout {

    private static final int CELL_WIDTH = 60;
    private static final int CONTENT_ROWS = 30;
    private static final int FRAME_WIDTH = (CELL_WIDTH + 2) * 2 + 1;
    private static final int TOP_ROW = 1;
    private static final int LEFT_COL = 1;
    private static final String ANSI_REGEX = "\u001B\\[[;\\d]*m";

    private final FrameType frameType;
    private final Map<Region, Panel> panels = new HashMap<>();
    private int inputRow;
    private InputMode inputMode = InputMode.REQUEST;
    private String errorMessage = null;

    public enum InputMode {
        REQUEST,
        CONFIRM,
        ACTION
    }

    public ScreenLayout(FrameType frameType) {
        this.frameType = frameType;
    }

    public void setPanel(Region region, Panel panel) {
        if (!frameType.supports(region)) {
            throw new IllegalArgumentException(
                    "Region " + region + " not supported by " + frameType);
        }
        panels.put(region, panel);
    }

    public void setInputMode(InputMode mode) {
        this.inputMode = mode;
    }

    public void setErrorMessage(String error) {
        this.errorMessage = error;
    }

    public int getInputRow() {
        return inputRow;
    }

    public int getInputCol() {
        return LEFT_COL + 4;
    }

    public void render(String title) {
        ConsoleUI.clear();
        drawFrame(title);
        switch (frameType) {
            case SINGLE ->
                drawSingle(panels.get(Region.MAIN).render());
            case DOUBLE_VERTICAL ->
                drawDoubleVertical(
                        panels.get(Region.TOP).render(),
                        panels.get(Region.BOTTOM).render());
            case DOUBLE_HORIZONTAL ->
                drawDoubleHorizontal(
                        panels.get(Region.LEFT).render(),
                        panels.get(Region.RIGHT).render());
            case QUAD ->
                drawQuad(
                        panels.get(Region.TOP_LEFT).render(),
                        panels.get(Region.TOP_RIGHT).render(),
                        panels.get(Region.BOTTOM_LEFT).render(),
                        panels.get(Region.BOTTOM_RIGHT).render());
        }
        drawInputPrompt(inputMode, errorMessage);
        clearBelowFrame();
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
        int half = CONTENT_ROWS / 2;
        for (int i = 0; i < half; i++) {
            String line = i < top.size() ? top.get(i) : "";
            put(TOP_ROW + 3 + i, "│ " + pad(line, FRAME_WIDTH - 2) + " │");
        }
        put(TOP_ROW + 3 + half, "├" + "─".repeat(FRAME_WIDTH) + "┤");
        for (int i = 0; i < half; i++) {
            String line = i < bottom.size() ? bottom.get(i) : "";
            put(TOP_ROW + 4 + half + i, "│ " + pad(line, FRAME_WIDTH - 2) + " │");
        }
    }

    private void drawDoubleHorizontal(List<String> left, List<String> right) {
        for (int i = 0; i < CONTENT_ROWS; i++) {
            String l = i < left.size() ? left.get(i) : "";
            String r = i < right.size() ? right.get(i) : "";
            put(TOP_ROW + 3 + i,
                    "│ " + pad(l, CELL_WIDTH) + " │ " + pad(r, CELL_WIDTH) + " │");
        }
    }

    private void drawQuad(List<String> tl, List<String> tr,
            List<String> bl, List<String> br) {
        int half = CONTENT_ROWS / 2;
        for (int i = 0; i < half; i++) {
            String l = i < tl.size() ? tl.get(i) : "";
            String r = i < tr.size() ? tr.get(i) : "";
            put(TOP_ROW + 3 + i,
                    "│ " + pad(l, CELL_WIDTH) + " │ " + pad(r, CELL_WIDTH) + " │");
        }
        put(TOP_ROW + 3 + half,
                "├" + "─".repeat(CELL_WIDTH + 2) + "┼" + "─".repeat(CELL_WIDTH + 2) + "┤");
        for (int i = 0; i < half; i++) {
            String l = i < bl.size() ? bl.get(i) : "";
            String r = i < br.size() ? br.get(i) : "";
            put(TOP_ROW + 4 + half + i,
                    "│ " + pad(l, CELL_WIDTH) + " │ " + pad(r, CELL_WIDTH) + " │");
        }
    }

    private void drawInputPrompt(InputMode mode, String error) {
        int footerTop = TOP_ROW + 3 + CONTENT_ROWS;
        put(footerTop, "├" + "─".repeat(FRAME_WIDTH) + "┤");

        String label;
        if (error != null) {
            label = "Error: " + error; 
        }else if (mode == InputMode.CONFIRM) {
            label = "Confirm? (y/n)"; 
        }else if (mode == InputMode.REQUEST) {
            label = "Input the following field";
        } else {
            label = "Select your action";
        }

        put(footerTop + 1, "│ " + pad(label, FRAME_WIDTH - 2) + " │");
        put(footerTop + 2, "│ > " + pad("", FRAME_WIDTH - 4) + " │");
        put(footerTop + 3, "└" + "─".repeat(FRAME_WIDTH) + "┘");

        inputRow = footerTop + 2; // blank line where the user types
    }

    private void put(int row, String text) {
        ConsoleUI.moveCursor(row, LEFT_COL);
        ConsoleUI.print(text);
    }

    private String center(String s, int width) {
        int pad = Math.max(0, width - visibleLength(s));
        return " ".repeat(pad / 2) + s + " ".repeat(pad - pad / 2);
    }

    private String pad(String s, int width) {
        int visible = visibleLength(s);
        if (visible >= width) {
            return s;
        }
        return s + " ".repeat(width - visible);
    }

    private int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    private String stripAnsi(String s) {
        return s.replaceAll(ANSI_REGEX, "");
    }

    private void clearBelowFrame() {
        int frameBottom = TOP_ROW + 3 + CONTENT_ROWS + 4; // last row of frame
        ConsoleUI.moveCursor(frameBottom + 1, 1);
        ConsoleUI.print("\033[J"); // erase from cursor to end of screen
    }
    public void parkCursor() {
        ConsoleUI.moveCursor(inputRow, getInputCol());
        ConsoleUI.flush();
    }
}
