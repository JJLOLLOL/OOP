package ui.panels;

import ui.ConsoleUI;

public class HeaderPanel extends BasePanel {

  public HeaderPanel(){
    super(1,1,46,3);
  }

  @Override
  public void render(){

    drawBorder();
    ConsoleUI.moveCursor(row+1, col+2);
    System.out.print("CREATE YOUR SIM");

  }
}