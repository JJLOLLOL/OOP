package ui;

import core.GameEngine;

import java.util.Scanner;

public class MainState implements GameState {
    protected Scanner scanner;

    public MainState(){
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void render(GameEngine engine) {
        System.out.println("Select an option: ");
        String input = scanner.nextLine();
        handleInput(input, engine);
    }

    @Override
    public void handleInput(String input, GameEngine engine) {
        if (input.equals("3")) {
            System.out.println("Ending game!");
            engine.end();
        }
    }

}
