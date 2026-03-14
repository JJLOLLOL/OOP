package ui.states;

import core.GameEngine;
import ui.ConsoleUI;
import ui.panels.CreateSimPanel;
import ui.screens.InitialisationScreen;
import java.util.List;
import java.util.Scanner;

public class InitialisationState implements State<List<String>> {

    private Scanner scanner = new Scanner(System.in);
    private InitialisationScreen screen = new InitialisationScreen();

    @Override
    public void render(GameEngine engine) {

        ConsoleUI.clear();
        screen.render();
        CreateSimPanel form = screen.getForm();

        form.nameCursor();
        String name = scanner.nextLine();
        form.ageCursor();
        String age = scanner.nextLine();
        form.genderCursor();
        String gender = scanner.nextLine();

        handleInput(List.of(name,age,gender), engine);
    }

    @Override
    public void handleInput(List<String> input, GameEngine engine) {
        String name = input.get(0);
        int age = Integer.parseInt(input.get(1));
        String gender = input.get(2);

        // engine.setGameState(new MainState());
    }

    @Override
    public void update(GameEngine engine, double deltaTime){}
}