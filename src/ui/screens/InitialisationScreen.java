package ui.screens;

import ui.ConsoleUI;
import ui.panels.CreateSimPanel;
import ui.panels.HeaderPanel;

public class InitialisationScreen implements Screen {

  private HeaderPanel header = new HeaderPanel();
  private CreateSimPanel form = new CreateSimPanel();

  @Override
  public void render(){

    ConsoleUI.resetCursor();
    header.render();
    form.render();
  }

  public CreateSimPanel getForm(){
    return form;
  }

}