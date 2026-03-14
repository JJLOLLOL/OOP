package ui.states;

import core.GameEngine;

public interface State<T> {

    public void render(GameEngine engine);

    void handleInput(T input, GameEngine engine);
    
    void update(GameEngine engine, double deltaTime);
}