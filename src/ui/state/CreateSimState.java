package ui.state;

import core.GameEngine;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import models.Career;
import models.CareerList;
import models.Location;
import models.SimCharacter;
import ui.layout.ScreenLayout;
import ui.panel.CreateSimActionPanel;
import ui.panel.CreateSimListPanel;
import ui.screen.CreateSimScreen;
import ui.screen.Screen;

public class CreateSimState extends BaseState<List<String[]>> {

    private final CreateSimScreen screen = new CreateSimScreen();

    private enum Step {
        COUNT,
        NAME, AGE, GENDER, // create flow
        OPTIONS, // keep / delete just-entered sim
        SELECT, // all slots filled — pick a sim number or C to confirm
        SELECT_ACTION, // edit / delete chosen sim
        EDIT_NAME, EDIT_AGE, EDIT_GENDER, // edit flow (same fields, different next-step)
        PICK_PLAYER
    }

    private Step currentStep = Step.COUNT;

    private int totalSims = 0;
    private int currentSimIndex = 0;  // slot being filled during create/edit flow
    private int selectedSim = -1; // 0-based index of sim chosen in SELECT

    // in-flight fields — shared by both create and edit flows
    private String name = "", age = "", gender = "";

    // committed sim data — source of truth, pushed to listPanel on every change
    private final List<String[]> committedSims = new ArrayList<>();

    // built in handleInput() once all sims are confirmed
    private final List<SimCharacter> createdSims = new ArrayList<>();

    @Override
    protected Screen getScreen() {
        return screen;
    }

    // ── Convenience accessors ─────────────────────────────────────────────────
    private CreateSimListPanel listPanel() {
        return screen.getListPanel();
    }

    private CreateSimActionPanel actionPanel() {
        return screen.getActionPanel();
    }

    private ScreenLayout layout() {
        return screen.getLayout();
    }

    // ── Update ────────────────────────────────────────────────────────────────
    @Override
    public void update(GameEngine engine, double deltaTime) {
        String input = engine.pollInput();
        if (input == null) {
            return;
        }
        dirty = true;

        switch (currentStep) {

            case COUNT -> {
                try {
                    int count = Integer.parseInt(input.trim());
                    if (count < 1) {
                        throw new NumberFormatException();
                    }
                    totalSims = count;
                    currentSimIndex = 0;
                    layout().setErrorMessage(null);
                    startEntering(1);
                } catch (NumberFormatException e) {
                    layout().setErrorMessage("Enter a valid number greater than 0.");
                    layout().setInputMode(ScreenLayout.InputMode.CREATESIM);
                }
            }

            // ── NAME / AGE / GENDER (shared by create and edit) ───────────────
            case NAME, EDIT_NAME -> {
                name = input.trim();
                actionPanel().setName(name);
                layout().setErrorMessage(null);
                currentStep = (currentStep == Step.NAME) ? Step.AGE : Step.EDIT_AGE;
            }

            case AGE, EDIT_AGE -> {
                try {
                    Integer.parseInt(input.trim());
                    age = input.trim();
                    actionPanel().setAge(age);
                    layout().setErrorMessage(null);
                    currentStep = (currentStep == Step.AGE) ? Step.GENDER : Step.EDIT_GENDER;
                } catch (NumberFormatException e) {
                    layout().setErrorMessage("Age must be a number.");
                }
            }

            case GENDER, EDIT_GENDER -> {
                gender = input.trim();
                actionPanel().setGender(gender);
                layout().setErrorMessage(null);

                if (currentStep == Step.GENDER) {
                    actionPanel().setMode(CreateSimActionPanel.Mode.OPTIONS);
                    currentStep = Step.OPTIONS;
                    layout().setInputMode(ScreenLayout.InputMode.ACTION);
                } else {
                    // edit complete — re-insert at same slot and return to SELECT
                    committedSims.add(selectedSim, new String[]{name, age, gender});
                    syncListPanel();
                    clearInFlight();
                    enterSelect();
                }
            }

            case OPTIONS -> {
                switch (input.trim()) {
                    case "1" -> {
                        committedSims.add(new String[]{name, age, gender});
                        syncListPanel();
                        clearInFlight();
                        currentSimIndex++;
                        layout().setErrorMessage(null);

                        if (currentSimIndex < totalSims) {
                            startEntering(currentSimIndex + 1);
                        } else {
                            enterSelect();
                        }
                    }
                    case "2" -> {
                        // discard in-flight, redo this slot
                        clearInFlight();
                        layout().setErrorMessage(null);
                        startEntering(currentSimIndex + 1);
                    }
                    default ->
                        layout().setErrorMessage("Enter 1 to keep or 2 to delete.");
                }
            }

            case SELECT -> {
                if (input.trim().equalsIgnoreCase("c")) {
                    layout().setErrorMessage(null);
                    handleInput(committedSims, engine);
                } else {
                    try {
                        int pick = Integer.parseInt(input.trim());
                        if (pick < 1 || pick > committedSims.size()) {
                            throw new NumberFormatException();
                        }
                        selectedSim = pick - 1;
                        listPanel().setSelectedSim(selectedSim);
                        actionPanel().setMode(CreateSimActionPanel.Mode.SELECT_ACTION);
                        actionPanel().setSelectedSim(selectedSim);
                        layout().setErrorMessage(null);
                        currentStep = Step.SELECT_ACTION;
                        layout().setInputMode(ScreenLayout.InputMode.ACTION);
                    } catch (NumberFormatException e) {
                        layout().setErrorMessage(
                                "Enter a sim number (1-" + committedSims.size() + ") to manage, or C to confirm.");
                    }
                }
            }

            case SELECT_ACTION -> {
                switch (input.trim()) {
                    case "1" -> {
                        // load existing data and remove slot for re-entry
                        String[] sim = committedSims.remove(selectedSim);
                        name = sim[0];
                        age = sim[1];
                        gender = sim[2];
                        syncListPanel();
                        listPanel().setSelectedSim(-1);
                        actionPanel().setName(name);
                        actionPanel().setAge(age);
                        actionPanel().setGender(gender);
                        actionPanel().setMode(CreateSimActionPanel.Mode.ENTERING);
                        actionPanel().setSimNumber(selectedSim + 1);
                        layout().setErrorMessage(null);
                        currentStep = Step.EDIT_NAME;
                        layout().setInputMode(ScreenLayout.InputMode.REQUEST);
                    }
                    case "2" -> {
                        committedSims.remove(selectedSim);
                        syncListPanel();
                        listPanel().setSelectedSim(-1);
                        totalSims--;
                        layout().setErrorMessage(null);

                        if (totalSims == 0) {
                            fullReset(); 
                        }else {
                            enterSelect();
                        }
                    }
                    default ->
                        layout().setErrorMessage("Enter 1 to edit or 2 to delete.");
                }
            }

            case PICK_PLAYER -> {
                try {
                    int choice = Integer.parseInt(input.trim());
                    if (choice < 1 || choice > createdSims.size()) {
                        throw new NumberFormatException();
                    }
                    layout().setErrorMessage(null);
                    engine.setActivePlayer(createdSims.get(choice - 1));
                    engine.setGameState(new MainState());
                } catch (NumberFormatException e) {
                    layout().setErrorMessage("Enter a number between 1 and " + createdSims.size() + ".");
                }
            }
        }
    }

    // ── handleInput ───────────────────────────────────────────────────────────
    @Override
    public void handleInput(List<String[]> sims, GameEngine engine) {
        Location home = WorldRegistry.getInstance().getLocation("Home");
        Career jobless = new Career(CareerList.JOBLESS);

        for (String[] data : sims) {
            SimCharacter sim = new SimCharacter(data[0], Integer.parseInt(data[1]), data[2], home, jobless);
            engine.getRelationshipManager().registerNewSim(
                    sim, engine.getSims(), WorldRegistry.getInstance().getAllNPCs());
            engine.getSims().add(sim);
            createdSims.add(sim);
        }

        if (createdSims.size() == 1) {
            engine.setActivePlayer(createdSims.get(0));
            engine.setGameState(new MainState());
        } else {
            actionPanel().setMode(CreateSimActionPanel.Mode.PICK_PLAYER);
            actionPanel().setPickList(createdSims);
            currentStep = Step.PICK_PLAYER;
            layout().setInputMode(ScreenLayout.InputMode.REQUEST);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    /**
     * Sets up the action panel and step for entering a new sim at slot n.
     */
    private void startEntering(int simNumber) {
        actionPanel().setMode(CreateSimActionPanel.Mode.ENTERING);
        actionPanel().setSimNumber(simNumber);
        currentStep = Step.NAME;
        layout().setInputMode(ScreenLayout.InputMode.REQUEST);
    }

    /**
     * Transitions to SELECT — always allows C to confirm or a number to manage.
     */
    private void enterSelect() {
        actionPanel().setMode(CreateSimActionPanel.Mode.EMPTY);
        listPanel().setSelectedSim(-1);
        currentStep = Step.SELECT;
        layout().setInputMode(ScreenLayout.InputMode.SELECT);
    }

    /**
     * Pushes current committedSims list to the list panel.
     */
    private void syncListPanel() {
        listPanel().setSims(committedSims);
    }

    /**
     * Clears all in-flight fields in state and action panel.
     */
    private void clearInFlight() {
        name = "";
        age = "";
        gender = "";
        actionPanel().setName("");
        actionPanel().setAge("");
        actionPanel().setGender("");
    }

    /**
     * Full reset back to COUNT — used when all sims are deleted.
     */
    private void fullReset() {
        totalSims = 0;
        currentSimIndex = 0;
        selectedSim = -1;
        committedSims.clear();
        createdSims.clear();
        listPanel().reset();
        actionPanel().reset();
        currentStep = Step.COUNT;
        layout().setInputMode(ScreenLayout.InputMode.CREATESIM);
        layout().setErrorMessage(null);
    }
}
