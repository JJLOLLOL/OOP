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
        COUNT, // ask how many sims to create
        NAME, // enter name for current sim slot
        AGE, // enter age for current sim slot
        GENDER, // enter gender for current sim slot
        OPTIONS, // keep(1) / delete(2) for the just-entered sim
        SELECT, // all slots filled — type a sim number to manage, or C to confirm
        SELECT_ACTION, // chosen sim — edit(1) / delete(2)
        EDIT_NAME, // re-enter name for selected sim
        EDIT_AGE, // re-enter age for selected sim
        EDIT_GENDER, // re-enter gender for selected sim
        PICK_PLAYER     // choose which sim becomes the active player
    }

    private Step currentStep = Step.COUNT;

    private int totalSims = 0;
    private int currentSimIndex = 0;   // slot being filled during CREATE flow
    private int selectedSim = -1;  // slot chosen during SELECT

    // in-flight fields for the sim currently being entered / edited
    private String name = "", age = "", gender = "";

    // committed sim data — kept in sync with listPanel
    private final List<String[]> committedSims = new ArrayList<>();

    // finalised SimCharacter objects, built in handleInput()
    private final List<SimCharacter> createdSims = new ArrayList<>();

    @Override
    protected Screen getScreen() {
        return screen;
    }

    @Override
    public void update(GameEngine engine, double deltaTime) {
        String input = engine.pollInput();
        if (input == null) {
            return;
        }

        dirty = true;
        CreateSimListPanel listPanel = screen.getListPanel();
        CreateSimActionPanel actionPanel = screen.getActionPanel();
        ScreenLayout layout = screen.getLayout();

        switch (currentStep) {

            // ── COUNT ─────────────────────────────────────────────────────────
            case COUNT -> {
                try {
                    int count = Integer.parseInt(input.trim());
                    if (count < 1) {
                        throw new NumberFormatException();
                    }
                    totalSims = count;
                    currentSimIndex = 0;
                    layout.setErrorMessage(null);
                    // show first sim entry on the right
                    actionPanel.setMode(CreateSimActionPanel.Mode.ENTERING);
                    actionPanel.setSimNumber(1);
                    currentStep = Step.NAME;
                    layout.setInputMode(ScreenLayout.InputMode.REQUEST);
                } catch (NumberFormatException e) {
                    layout.setErrorMessage("Enter a valid number greater than 0.");
                    layout.setInputMode(ScreenLayout.InputMode.CREATESIM);
                }
            }

            // ── CREATE FLOW ───────────────────────────────────────────────────
            case NAME -> {
                name = input.trim();
                actionPanel.setName(name);
                layout.setErrorMessage(null);
                currentStep = Step.AGE;
            }

            case AGE -> {
                try {
                    Integer.parseInt(input.trim());
                    age = input.trim();
                    actionPanel.setAge(age);
                    layout.setErrorMessage(null);
                    currentStep = Step.GENDER;
                } catch (NumberFormatException e) {
                    layout.setErrorMessage("Age must be a number.");
                }
            }

            case GENDER -> {
                gender = input.trim();
                actionPanel.setGender(gender);
                actionPanel.setMode(CreateSimActionPanel.Mode.OPTIONS);
                layout.setErrorMessage(null);
                currentStep = Step.OPTIONS;
                layout.setInputMode(ScreenLayout.InputMode.ACTION);
            }

            case OPTIONS -> {
                switch (input.trim()) {
                    case "1" -> {
                        // Keep — commit to list
                        committedSims.add(new String[]{name, age, gender});
                        listPanel.setSims(committedSims);
                        layout.setErrorMessage(null);
                        currentSimIndex++;
                        clearInFlight(actionPanel);

                        if (currentSimIndex < totalSims) {
                            actionPanel.setMode(CreateSimActionPanel.Mode.ENTERING);
                            actionPanel.setSimNumber(currentSimIndex + 1);
                            currentStep = Step.NAME;
                            layout.setInputMode(ScreenLayout.InputMode.REQUEST);
                        } else {
                            enterSelect(listPanel, actionPanel, layout);
                        }
                    }
                    case "2" -> {
                        // Delete — discard in-flight, redo this slot
                        clearInFlight(actionPanel);
                        actionPanel.setMode(CreateSimActionPanel.Mode.ENTERING);
                        actionPanel.setSimNumber(currentSimIndex + 1);
                        layout.setErrorMessage(null);
                        currentStep = Step.NAME;
                        layout.setInputMode(ScreenLayout.InputMode.REQUEST);
                    }
                    default ->
                        layout.setErrorMessage("Enter 1 to keep or 2 to delete.");
                }
            }

            // ── SELECT ────────────────────────────────────────────────────────
            case SELECT -> {
                if (input.trim().equalsIgnoreCase("c")) {
                    layout.setErrorMessage(null);
                    handleInput(committedSims, engine);
                } else {
                    try {
                        int pick = Integer.parseInt(input.trim());
                        if (pick < 1 || pick > committedSims.size()) {
                            throw new NumberFormatException();
                        }
                        selectedSim = pick - 1;
                        listPanel.setSelectedSim(selectedSim);
                        actionPanel.setMode(CreateSimActionPanel.Mode.SELECT_ACTION);
                        actionPanel.setSelectedSim(selectedSim);
                        layout.setErrorMessage(null);
                        currentStep = Step.SELECT_ACTION;
                        layout.setInputMode(ScreenLayout.InputMode.ACTION);
                    } catch (NumberFormatException e) {
                        layout.setErrorMessage(
                                "Enter a sim number (1-" + committedSims.size()
                                + ") to manage, or C to confirm.");
                    }
                }
            }

            case SELECT_ACTION -> {
                switch (input.trim()) {
                    case "1" -> {
                        // Edit — load existing data, remove from committed list
                        String[] sim = committedSims.get(selectedSim);
                        name = sim[0];
                        age = sim[1];
                        gender = sim[2];
                        committedSims.remove(selectedSim);
                        listPanel.setSims(committedSims);
                        listPanel.setSelectedSim(-1);
                        actionPanel.setMode(CreateSimActionPanel.Mode.ENTERING);
                        actionPanel.setSimNumber(selectedSim + 1);
                        actionPanel.setName(name);
                        actionPanel.setAge(age);
                        actionPanel.setGender(gender);
                        layout.setErrorMessage(null);
                        currentStep = Step.EDIT_NAME;
                        layout.setInputMode(ScreenLayout.InputMode.REQUEST);
                    }
                    case "2" -> {
                        // Delete
                        committedSims.remove(selectedSim);
                        listPanel.setSims(committedSims);
                        listPanel.setSelectedSim(-1);
                        totalSims--;
                        layout.setErrorMessage(null);

                        if (totalSims == 0) {
                            fullReset(listPanel, actionPanel, layout);
                        } else {
                            enterSelect(listPanel, actionPanel, layout);
                        }
                    }
                    default ->
                        layout.setErrorMessage("Enter 1 to edit or 2 to delete.");
                }
            }

            // ── EDIT FLOW ─────────────────────────────────────────────────────
            case EDIT_NAME -> {
                name = input.trim();
                actionPanel.setName(name);
                layout.setErrorMessage(null);
                currentStep = Step.EDIT_AGE;
            }

            case EDIT_AGE -> {
                try {
                    Integer.parseInt(input.trim());
                    age = input.trim();
                    actionPanel.setAge(age);
                    layout.setErrorMessage(null);
                    currentStep = Step.EDIT_GENDER;
                } catch (NumberFormatException e) {
                    layout.setErrorMessage("Age must be a number.");
                }
            }

            case EDIT_GENDER -> {
                gender = input.trim();
                actionPanel.setGender(gender);
                // Re-insert edited sim at same slot
                committedSims.add(selectedSim, new String[]{name, age, gender});
                listPanel.setSims(committedSims);
                clearInFlight(actionPanel);
                layout.setErrorMessage(null);
                enterSelect(listPanel, actionPanel, layout);
            }

            // ── PICK PLAYER ───────────────────────────────────────────────────
            case PICK_PLAYER -> {
                try {
                    int choice = Integer.parseInt(input.trim());
                    if (choice < 1 || choice > createdSims.size()) {
                        throw new NumberFormatException();
                    }
                    layout.setErrorMessage(null);
                    engine.setActivePlayer(createdSims.get(choice - 1));
                    engine.setGameState(new MainState());
                } catch (NumberFormatException e) {
                    layout.setErrorMessage(
                            "Enter a number between 1 and " + createdSims.size() + ".");
                }
            }
        }
    }

    @Override
    public void handleInput(List<String[]> sims, GameEngine engine) {
        for (String[] data : sims) {
            String n = data[0];
            int a = Integer.parseInt(data[1]);
            String g = data[2];

            Location defaultLocation = WorldRegistry.getInstance().getLocation("Home");
            Career startingCareer = new Career(CareerList.JOBLESS);
            SimCharacter sim = new SimCharacter(n, a, g, defaultLocation, startingCareer);

            engine.getRelationshipManager().registerNewSim(
                    sim, engine.getSims(), WorldRegistry.getInstance().getAllNPCs());

            createdSims.add(sim);
        }

        if (createdSims.size() == 1) {
            engine.setActivePlayer(createdSims.get(0));
            engine.setGameState(new MainState());
        } else {
            screen.getActionPanel().setMode(CreateSimActionPanel.Mode.PICK_PLAYER);
            screen.getActionPanel().setPickList(createdSims);
            currentStep = Step.PICK_PLAYER;
            screen.getLayout().setInputMode(ScreenLayout.InputMode.REQUEST);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void enterSelect(CreateSimListPanel listPanel,
            CreateSimActionPanel actionPanel,
            ScreenLayout layout) {
        listPanel.setSelectedSim(-1);
        if (totalSims == 1) {
            // Only one sim — skip SELECT, go straight to SELECT_ACTION
            selectedSim = 0;
            listPanel.setSelectedSim(0);
            actionPanel.setMode(CreateSimActionPanel.Mode.SELECT_ACTION);
            actionPanel.setSelectedSim(0);
            currentStep = Step.SELECT_ACTION;
            layout.setInputMode(ScreenLayout.InputMode.ACTION);
        } else {
            actionPanel.setMode(CreateSimActionPanel.Mode.EMPTY);
            currentStep = Step.SELECT;
            layout.setInputMode(ScreenLayout.InputMode.SELECT);
        }
    }

    private void clearInFlight(CreateSimActionPanel actionPanel) {
        name = "";
        age = "";
        gender = "";
        actionPanel.setName("");
        actionPanel.setAge("");
        actionPanel.setGender("");
    }

    private void fullReset(CreateSimListPanel listPanel,
            CreateSimActionPanel actionPanel,
            ScreenLayout layout) {
        totalSims = 0;
        currentSimIndex = 0;
        selectedSim = -1;
        committedSims.clear();
        createdSims.clear();
        listPanel.reset();
        actionPanel.reset();
        currentStep = Step.COUNT;
        layout.setInputMode(ScreenLayout.InputMode.CREATESIM);
        layout.setErrorMessage(null);
    }
}
