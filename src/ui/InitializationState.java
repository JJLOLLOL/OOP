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
import java.util.TreeMap;

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
        System.out.println("Enter your SIM's age:");
        String age = scanner.nextLine();
        System.out.println("Enter your SIM's gender:");
        String gender = scanner.nextLine();
        ArrayList<String> inputList = new ArrayList<>();
        inputList.add(name);
        inputList.add(age);
        inputList.add(gender);
        handleInput(inputList, engine);
    }

    @Override
    public void handleInput(List<String> inputList, GameEngine engine) {
//        System.out.println("Welcome " + input + "!");

        // player = new SimCharacter(input, 20, "Non-binary");

        ArrayList<Location> bellaLocations = new ArrayList<>();
        bellaLocations.add(new Location("Home", new ArrayList<>()));
        bellaLocations.add(new Location("Work", new ArrayList<>()));
        bellaLocations.add(new Location("Restaurant", new ArrayList<>()));
        NPCCharacter bella = new NPCCharacter("Bella Goth", 25, "Female", bellaLocations);

        ArrayList<Location> mortimerLocations = new ArrayList<>();
        mortimerLocations.add(new Location("Home", new ArrayList<>()));
        mortimerLocations.add(new Location("Work", new ArrayList<>()));
        mortimerLocations.add(new Location("Restaurant", new ArrayList<>()));
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

        // RELATIONSHIP STATUS LEVELS (Score-based):
        // ─────────────────────────────────────────────────────────────────────────
        // Score Range        | Status
        // ─────────────────────────────────────────────────────────────────────────
        // -100 to -50        | Enemy (major conflict, gossip, sabotage)
        // -50 to -25         | Disliked (tension, avoidance)
        // -25 to 25          | Acquaintance (neutral, basic interaction)
        // 25 to 50           | Friendly (positive, cooperative)
        // 50 to 70           | Friend (good relationship, sharing benefits)
        // 70 to 100          | Best Friend (very close, major bonuses)
        //
        // MORTIMER GOTH - UNIQUE RELATIONSHIP BENEFITS:
        // ─────────────────────────────────────────────────────────────────────────
        // Special Case: Mortimer's relationships decay SLOWER and have a FLOOR at 0
        // 1) Decay only happens every 3 months (not every month)
        // 2) Score cannot go below 0 unless explicit negative action taken
        // 3) Relationship status for Mortimer:
        //    - Disliked (-50 to -25): Tense and distant
        //    - Friendly (25+): Positive mentor
        //    - Friend (50+): Career networking benefits
        //    - Best Friend (70+): Inheritance/business opportunities
        //
        // Example GameEngine implementation (monthly update):
        // ─────────────────────────────────────────────────────────────────────────
        // relationshipManager.forEachRelationship((from, to, relation) -> {
        //   // MORTIMER: Slow decay with floor at 0
        //   if (to.getName().equals("Mortimer Goth") && currentMonth % 3 == 0) {
        //     if (relation.getScore() > 0) {
        //       relation.changeScore(-1);  // Decay every 3 months
        //     }
        //     // IMPORTANT: Score STOPS at 0, never goes negative (natural floor)
        //   }
        //
        //   // MORTIMER CAREER BONUS (50+ Friend status)
        //   if (from instanceof SimCharacter && to.getName().equals("Mortimer Goth")) {
        //     SimCharacter sim = (SimCharacter) from;
        //     if (relation.getScore() >= 50) {
        //       sim.getSkills().addSkill("Business", 5);  // Career networking bonus
        //     }
        //   }
        //
        //   // MORTIMER BEST FRIEND INHERITANCE (70+ Best Friend status)
        //   if (from instanceof SimCharacter && to.getName().equals("Mortimer Goth")) {
        //     SimCharacter sim = (SimCharacter) from;
        //     if (relation.getScore() >= 70) {
        //       sim.setMoney(sim.getMoney() + 1000);  // Inheritance bonus
        //     }
        //   }
        // });
        //
        // OTHER RELATIONSHIP EXAMPLES (Using Status Levels):
        // ─────────────────────────────────────────────────────────────────────────
        // 1) BELLA - Rich Stipend Bonus (50+ Friend status)
        // relationshipManager.forEachRelationship((from, to, relation) -> {
        //   if (from instanceof SimCharacter && to.getName().equals("Bella Goth")) {
        //     SimCharacter sim = (SimCharacter) from;
        //     // At Friend status (50+), get monthly allowance from wealthy Bella
        //     if (relation.getStatus().equals("Friend") && sim.getMoney() < 2000) {
        //       sim.setMoney(sim.getMoney() + 200);  // Monthly stipend
        //     }
        //   }
        // });
        //
        // 2) ENEMY RELATIONSHIPS - Stress Penalty (-50+ Enemy status)
        // relationshipManager.forEachRelationship((from, to, relation) -> {
        //   if (from instanceof SimCharacter && relation.getStatus().equals("Enemy")) {
        //     SimCharacter sim = (SimCharacter) from;
        //     sim.setMoney(sim.getMoney() - 50);  // Stress affects finances
        //   }
        // });
        //
        // 3) FRIEND SKILL SHARING - (50+ Friend status with shared interests)
        // relationshipManager.forEachRelationship((from, to, relation) -> {
        //   if (from instanceof SimCharacter && to instanceof SimCharacter) {
        //     if (relation.getStatus().equals("Friend")) {
        //       // Share cooking skill if both interested in cooking
        //       ((SimCharacter) from).getSkills().addSkill("Cooking", 2);
        //     }
        //   }
        // });
        //
        // 4) LOW RELATIONSHIPS - Avoidance (Acquaintance/Disliked status)
        // relationshipManager.forEachRelationship((from, to, relation) -> {
        //   if (relation.getStatus().equals("Disliked") || relation.getStatus().equals("Acquaintance")) {
        //     // Characters avoid each other, no group activities
        //     from.setLocation(null);  // Skip shared activities
        //   }
        // });
        //
        // 5) BEST FRIEND SUPPORT - Emotional/Financial Help (70+ Best Friend)
        // relationshipManager.forEachRelationship((from, to, relation) -> {
        //   if (from instanceof SimCharacter && to instanceof SimCharacter) {
        //     if (relation.getStatus().equals("Best Friend")) {
        //       // Best friend provides mutual support
        //       ((SimCharacter) from).setMoney(((SimCharacter) from).getMoney() + 300);
        //     }
        //   }
        // });

        // engine.setGameState(new MainState(player, npcCharacterList,
        // relationshipManager));
        engine.setGameState(new MainState());
    }

}
