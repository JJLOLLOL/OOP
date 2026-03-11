package ui.screens;

import core.GameEngine;
import ui.ConsoleUI;

public class InitialisationScreen implements Screen {

  @Override
  public void render(GameEngine engine) {

    ConsoleUI.resetCursor();
    drawFrame();

    ConsoleUI.moveCursor(7,7);
    System.out.print("Enter your SIM name:");

    ConsoleUI.moveCursor(9,9);
    System.out.print("> ");

    ConsoleUI.moveCursor(9,11);
  }

private void drawFrame() {

  ConsoleUI.moveCursor(1,1);
  System.out.print("┌──────────────────────────────────────────────┐");

  ConsoleUI.moveCursor(2,1);
  System.out.print("│             CREATE YOUR SIM                  │");

  ConsoleUI.moveCursor(3,1);
  System.out.print("├──────────────────────────────────────────────┤");

  for(int i = 4; i <= 12; i++){
      ConsoleUI.moveCursor(i,1);
      System.out.print("│                                              │");
  }

  ConsoleUI.moveCursor(13,1);
  System.out.print("└──────────────────────────────────────────────┘");
}
}