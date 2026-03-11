package ui.states;

import core.GameEngine;

public interface GameState<T> {
    void render(GameEngine engine);
    void handleInput(T input, GameEngine engine);
}