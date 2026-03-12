package ui.panels;

import ui.ConsoleUI;

public abstract class BasePanel implements Panel {

  protected int row;
  protected int col;
  protected int width;
  protected int height;

  public BasePanel(int row, int col, int width, int height){
    this.row = row;
    this.col = col;
    this.width = width;
    this.height = height;
  }

  protected void drawBorder(){

    ConsoleUI.moveCursor(row, col);
    System.out.print("┌" + "─".repeat(width-2) + "┐");

    for(int i=1;i<height-1;i++){
      ConsoleUI.moveCursor(row+i, col);
      System.out.print("│" + " ".repeat(width-2) + "│");
    }

    ConsoleUI.moveCursor(row+height-1, col);
    System.out.print("└" + "─".repeat(width-2) + "┘");
  }

}