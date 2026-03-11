package ui;

import core.GameEngine;
import models.SimCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainState extends BaseState {
    protected Scanner scanner;

    public MainState(){
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void render(GameEngine engine) {
        SimCharacter player = engine.getActivePlayer();
        System.out.println("Select an option: ");
        String option = scanner.nextLine();
        List<String> inputList = new ArrayList<>();
        inputList.add(option);
        handleInput(inputList, engine);
    }

    @Override
    public void handleInput(List<String> inputList, GameEngine engine) {
        if (inputList.getFirst().equals("3")) {
            System.out.println("Ending game!");
            engine.end();
        }
    }

}
