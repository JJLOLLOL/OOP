package ui.panels;

import core.GameEngine;

public abstract class Panel {

  protected int row;
  protected int col;

  public Panel(int row, int col) {
    this.row = row;
    this.col = col;
  }

  protected void moveCursor(int r, int c) {
    System.out.print("\033[" + r + ";" + c + "H");
  }

  public abstract void render(GameEngine engine);

}