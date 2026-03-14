package core;

import ui.states.InitialisationState;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start(new InitialisationState());
    }
}
