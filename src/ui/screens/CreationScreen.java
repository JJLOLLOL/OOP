package ui.screens;

import core.GameEngine;
import ui.panels.CharacterPanel;
import ui.panels.InfoPanel;

public class CreationScreen {

  private CharacterPanel characterPanel;
  private InfoPanel infoPanel;

  public CreationScreen(CharacterPanel characterPanel, InfoPanel infoPanel) {
    this.characterPanel = characterPanel;
    this.infoPanel = infoPanel;
  }

  public void render(GameEngine engine) {
    characterPanel.render(engine);
    infoPanel.render(engine);
  }
  
}