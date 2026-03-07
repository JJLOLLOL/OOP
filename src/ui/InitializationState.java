package ui;

import core.GameEngine;
import core.Main;
import models.NPCCharacter;
import models.SimCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InitializationState implements GameState {
    protected Scanner scanner;
    private SimCharacter player;
    private List<NPCCharacter> npcCharacterList;

    public InitializationState() {
        this.npcCharacterList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void render(GameEngine engine) {
        System.out.println("Welcome to CLI-based SIM!");
        System.out.println("=========================");
        System.out.println("Please setup your SIM!");
        System.out.println("Enter your SIM's name:");
        String name = scanner.nextLine();
        handleInput(name, engine);
    }

    @Override
    public void handleInput(String input, GameEngine engine) {
        System.out.println("Welcome " + input + "!");

        player = new SimCharacter(input, 20, "Non-binary");

        npcCharacterList.add(new NPCCharacter("Bella Goth", 25, "Female"));
        npcCharacterList.add(new NPCCharacter("Mortimer Goth", 30, "Male"));

        engine.setGameState(new MainState());
    }

}
