package ui.panels;

import ui.ConsoleUI;

public class CreateSimPanel extends BasePanel {

  public CreateSimPanel(){
    super(4,1,46,10);
  }

  @Override
  public void render(){

    drawBorder();

    ConsoleUI.moveCursor(row+2, col+3);
    System.out.print("Name:");

    ConsoleUI.moveCursor(row+4, col+3);
    System.out.print("Age:");

    ConsoleUI.moveCursor(row+6, col+3);
    System.out.print("Gender:");

  }

  public void nameCursor(){
    ConsoleUI.moveCursor(row+2, col+10);
  }

  public void ageCursor(){
    ConsoleUI.moveCursor(row+4, col+9);
  }

  public void genderCursor(){
    ConsoleUI.moveCursor(row+6, col+12);
  }
}