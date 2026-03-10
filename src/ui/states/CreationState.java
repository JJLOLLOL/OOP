package ui.states;

import core.GameEngine;
import core.GameState;
import ui.screens.CreationScreen;
import ui.panels.CharacterPanel;
import ui.panels.InfoPanel;

public class CreationState implements GameState {

  private enum Step {
    NAME,
    AGE,
    GENDER,
    DONE
  }

  private Step step = Step.NAME;

  private CharacterPanel characterPanel;
  private InfoPanel infoPanel;
  private CreationScreen screen;

  private String name;
  private String age;
  private String gender;

  public CreationState() {

    characterPanel = new CharacterPanel(3, 3);
    infoPanel = new InfoPanel(3, 35);

    screen = new CreationScreen(characterPanel, infoPanel);

  }

  @Override
  public void onEnter(GameEngine engine) {

    System.out.print("\033[2J");

    infoPanel.addLine("Create Your Character");
    infoPanel.addLine("---------------------");

    infoPanel.addLine("Name: ");
    infoPanel.addLine("Age: ");
    infoPanel.addLine("Gender: ");

  }

  @Override
  public void update(GameEngine engine) {

    characterPanel.update();

  }

  @Override
  public void render(GameEngine engine) {

    screen.render(engine);

  }

  @Override
  public void handleInput(String input, GameEngine engine) {

    switch (step) {

      case NAME -> {

        name = input;
        infoPanel.updateLine(2, "Name: " + name);
        step = Step.AGE;

      }

      case AGE -> {

        age = input;
        infoPanel.updateLine(3, "Age: " + age);
        step = Step.GENDER;

      }

      case GENDER -> {

        gender = input;
        infoPanel.updateLine(4, "Gender: " + gender);
        step = Step.DONE;

      }

      case DONE -> {
        // later transition
      }

    }

  }

}