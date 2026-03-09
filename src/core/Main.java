package core;

import ui.InitializationState;

public class Main {
    static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start(new InitializationState());
    }
}