package core;

import java.util.Scanner;
import states.CreationState;

public class GameEngine {

    private GameState currentState;

    public GameEngine() {
        currentState = new CreationState(this);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            currentState.render();
            String input = scanner.nextLine();
            currentState.handleInput(input);
            currentState.update();
        }
    }

    public void changeState(GameState newState) {
        currentState = newState;
    }

}