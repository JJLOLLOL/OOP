package ui;

import core.GameEngine;
import core.Main;
import models.Location;
import models.NPCCharacter;
// import models.SimCharacter;
import models.Location;
import models.Career;
import models.CareerList;
import services.RelationshipManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InitializationState implements GameState {
    protected Scanner scanner;
    // private SimCharacter player;
    private List<NPCCharacter> npcCharacterList;
    private RelationshipManager relationshipManager;

    public InitializationState() {
        this.npcCharacterList = new ArrayList<>();
        this.relationshipManager = new RelationshipManager();
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

        // player = new SimCharacter(input, 20, "Non-binary");

        ArrayList<Location> bellaLocations = new ArrayList<>();
        bellaLocations.add(new Location("Home", new ArrayList<>(), new ArrayList<>()));
        bellaLocations.add(new Location("Work", new ArrayList<>(), new ArrayList<>()));
        bellaLocations.add(new Location("Restaurant", new ArrayList<>(), new ArrayList<>()));
        NPCCharacter bella = new NPCCharacter("Bella Goth", 25, "Female", bellaLocations);

        ArrayList<Location> mortimerLocations = new ArrayList<>();
        mortimerLocations.add(new Location("Home", new ArrayList<>(), new ArrayList<>()));
        mortimerLocations.add(new Location("Work", new ArrayList<>(), new ArrayList<>()));
        mortimerLocations.add(new Location("Restaurant", new ArrayList<>(), new ArrayList<>()));
        NPCCharacter mortimer = new NPCCharacter("Mortimer Goth", 30, "Male", mortimerLocations);
        // NPCCharacter now supports two constructor patterns:
        // 1) Pass ArrayList<Location> and let NPC auto-generate a TreeMap schedule.
        // 2) Pass TreeMap<Integer, Location> if the engine builds the schedule directly.
        //
        // Example 1: Engine passes locations only
        // ArrayList<Location> customLocs = new ArrayList<>();
        // customLocs.add(new Location("Home", new ArrayList<>(), new ArrayList<>()));
        // customLocs.add(new Location("Park", new ArrayList<>(), new ArrayList<>()));
        // customLocs.add(new Location("Library", new ArrayList<>(), new ArrayList<>()));
        // NPCCharacter bookworm = new NPCCharacter("Bookworm", 22, "Male", customLocs);
        // npcCharacterList.add(bookworm);
        //
        // Example 2: Engine passes a pre-built schedule
        // TreeMap<Integer, Location> customSchedule = new TreeMap<>();
        // customSchedule.put(8, new Location("Home", new ArrayList<>(), new ArrayList<>()));
        // customSchedule.put(12, new Location("Work", new ArrayList<>(), new ArrayList<>()));
        // customSchedule.put(18, new Location("Gym", new ArrayList<>(), new ArrayList<>()));
        // NPCCharacter athlete = new NPCCharacter("Athlete", 25, "Male", customSchedule);
        // npcCharacterList.add(athlete);

        npcCharacterList.add(bella);
        npcCharacterList.add(mortimer);

        // Register characters with relationship manager
        // relationshipManager.register(player);
        // relationshipManager.register(bella);
        // relationshipManager.register(mortimer);

        // engine.setGameState(new MainState(player, npcCharacterList,
        // relationshipManager));
        engine.setGameState(new MainState());
    }

}
