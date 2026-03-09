package ui;

import core.GameEngine;

public interface GameState {
    void onEnter(GameEngine engine);
    void render(GameEngine engineState);
    void handleInput(String input, GameEngine engineState);
}
