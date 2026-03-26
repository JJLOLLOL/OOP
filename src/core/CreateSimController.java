package core;

import java.util.ArrayList;
import java.util.List;

import models.character.SimCharacter;
import models.location.Location;
import ui.Renderer;

public class CreateSimController {


    public enum Step {
        COUNT, NAME, AGE, GENDER, CONFIRM, PICK_PLAYER
    }


    private  final int MAX_SIMS = 5;

    private  Step step = Step.COUNT;
    private  int totalSims = 0;
    private  int currentIndex = 0;


    private  String name = "", age = "", gender = "";

    private  final List<String[]> committed = new ArrayList<>();

    public  boolean handleInput(String input, GameState state, WorldRegistry world) {
        switch (step) {

            case COUNT -> {
                try {
                    int n = Integer.parseInt(input);
                    if (n < 1 || n > MAX_SIMS) {
                        throw new NumberFormatException();
                    }
                    totalSims = n;
                    currentIndex = 0;
                    committed.clear();
                    setStep(Step.NAME);
                } catch (NumberFormatException e) {
                    Renderer.showError("Enter a number between 1 and " + MAX_SIMS + ".");
                    return false;
                }
            }

            case NAME -> {
                if (input.isBlank()) {
                    Renderer.showError("Name cannot be empty.");
                    return false;
                }
                name = input;
                setStep(Step.AGE);
            }

            case AGE -> {
                try {
                    int a = Integer.parseInt(input);
                    if (a < 10 || a > 90) {
                        throw new NumberFormatException();
                    }
                    age = input;
                    setStep(Step.GENDER);
                } catch (NumberFormatException e) {
                    Renderer.showError("Age must be a number between 10 and 90.");
                    return false;
                }
            }

            case GENDER -> {
                String g = input.trim().toUpperCase();
                if (!g.equals("M") && !g.equals("F")) {
                    Renderer.showError("Enter M for Male or F for Female.");
                    return false;
                }
                gender = g.equals("M") ? "Male" : "Female";
                committed.add(new String[]{name, age, gender});
                name = "";
                age = "";
                gender = "";
                setStep(++currentIndex < totalSims ? Step.NAME : Step.CONFIRM);
            }

            case CONFIRM -> {
                switch (input.toLowerCase()) {
                    case "y", "yes" ->
                        finaliseSims(state, world);
                    case "n", "no" -> {
                        committed.clear();
                        currentIndex = 0;
                        setStep(Step.COUNT);
                    }
                    default -> {
                        Renderer.showError("Enter Y to confirm or N to start over.");
                        return false;
                    }
                }
            }

            case PICK_PLAYER -> {
                try {
                    int pick = Integer.parseInt(input) - 1;
                    if (pick < 0 || pick >= state.getSims().size()) {
                        throw new NumberFormatException();
                    }
                    state.setActivePlayer(state.getSims().get(pick));
                    state.setPhase(GameState.Phase.PLAYING);
                } catch (NumberFormatException e) {
                    Renderer.showError("Enter a number between 1 and " + state.getSims().size() + ".");
                    return false;
                }
            }
        }

        return true;
    }

    private  void finaliseSims(GameState state, WorldRegistry world) {
        Location home = world.getLocation("Home");

        for (String[] data : committed) {
            SimCharacter sim = new SimCharacter(data[0], Integer.parseInt(data[1]), data[2], home);
            state.getRelationshipService().registerNewSim(sim, state.getSims(), world.getAllNPCs());
            state.addSim(sim);
            
            // Assign the shared global Home as the player's house
            sim.assignHouse((models.location.House) home);
        }

        if (state.getSims().size() == 1) {
            state.setActivePlayer(state.getSims().get(0));
            state.setPhase(GameState.Phase.PLAYING);
        } else {
            setStep(Step.PICK_PLAYER);
        }
    }

    private  void setStep(Step next) {
        step = next;
    }

    public  Step getStep() {
        return step;
    }
    public  int getTotalSims() {
        return totalSims;
    }
    public  int getCurrentIndex() {
        return currentIndex;
    }

    public  List<String[]> getCommitted() {
        return committed;
    }

    public  String getInFlightName() {
        return name;
    }

    public  String getInFlightAge() {
        return age;
    }
}
