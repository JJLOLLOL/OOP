package ui.panels;

import core.GameEngine;
import java.util.ArrayList;
import java.util.List;

public class InfoPanel extends Panel {

  private List<String> lines = new ArrayList<>();
  private String prompt = "";

  public InfoPanel(int row, int col) {
    super(row, col);
  }

  public void addLine(String line) {
    lines.add(line);
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }
public void updateLine(int index, String text) {

  if (index >= 0 && index < lines.size()) {
    lines.set(index, text);
  }

}
  @Override
  public void render(GameEngine engine) {

    int currentRow = row;

    // draw history
    for (String line : lines) {
      moveCursor(currentRow++, col);
      System.out.print(line + "      ");
    }

    // draw prompt
    if (!prompt.isEmpty()) {
      moveCursor(currentRow, col);
      System.out.print(prompt);
    }

  }

}