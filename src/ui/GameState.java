package ui;

import core.GameEngine;

import java.util.List;

public interface GameState {
    void render(GameEngine engine);
    void handleInput(List<String> input, GameEngine engine);
}
