package ui.state;

import core.GameEngine;
import ui.screen.Screen;

public abstract class BaseState<T> implements State<T> {

    protected boolean dirty = true;

    protected abstract Screen getScreen();

    @Override
    public void render(GameEngine engine) {
        if (!dirty) {
            return;
        }
        getScreen().render();
        getScreen().parkCursor();
        dirty = false;
    }
}
