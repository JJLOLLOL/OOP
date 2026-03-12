package ui.state;

import core.GameEngine;

public interface State<T> {

    public void render(GameEngine engine);

    void handleInput(T input, GameEngine engine);

}