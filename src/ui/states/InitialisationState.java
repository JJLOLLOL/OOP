package ui.states;

import core.GameEngine;
import ui.ConsoleUI;
import ui.screens.InitialisationScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InitialisationState implements GameState<List<String>> {

    private Scanner scanner = new Scanner(System.in);
    private InitialisationScreen screen = new InitialisationScreen();

    @Override
    public void render(GameEngine engine) {
        ConsoleUI.clear();
        screen.render(engine);
        List<String> input = new ArrayList<>();
        String name = scanner.nextLine();
        String age = scanner.nextLine();
        String gender = scanner.nextLine();
        input.add(name);
        input.add(age);
        input.add(gender);

        handleInput(input, engine);
    }

    @Override
    public void handleInput(List<String> input, GameEngine engine) {

        System.out.println("Welcome " + input.get(0));

        // engine.setGameState(new MainState());
    }
}