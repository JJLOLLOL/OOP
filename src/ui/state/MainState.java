package ui.state;

import core.GameEngine;
import models.SimCharacter;
import ui.panel.AttributePanel;
import ui.screen.MainScreen;

public class MainState implements State<Void> {

    private final MainScreen screen = new MainScreen();

    @Override
    public void render(GameEngine engine) {

        SimCharacter player = engine.getActivePlayer();
        AttributePanel panel = screen.getPanelTL();

        panel.setCharacter(
                player.getName(),
                player.getAge(),
                player.getMoney(),
                player.getNeeds()
        );

        screen.render();
    }

    @Override
    public void handleInput(Void input, GameEngine engine) {
        // future logic (movement, activity selection, etc.)
    }
}
