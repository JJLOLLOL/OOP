package core;

import ui.state.CreateSimState;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start(new CreateSimState());
    }
}
