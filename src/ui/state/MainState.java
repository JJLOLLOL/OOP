package ui.state;

import Types.InteractionType;
import core.GameEngine;
import core.InputQueue;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.Location;
import models.NPCCharacter;
import models.SimCharacter;
import models.furnitureactions.Furniture;
import ui.panel.ActionPanel;
import ui.panel.InteractablesPanel;
import ui.panel.LocationPanel;
import ui.panel.SocialisePanel;
import ui.screen.MainScreen;
import ui.screen.Screen;

public class MainState extends BaseState<String> {

    private final MainScreen screen = new MainScreen();
    private boolean initialized = false;

    private enum Step {
        MAIN,
        INTERACTABLES, INTERACTABLES_ACTION,
        SOCIALISE, SOCIALISE_ACTION,
        CHANGE_LOCATION
    }

    private Step currentStep = Step.MAIN;
    private Furniture selectedFurniture = null;
    private NPCCharacter selectedNPC = null;

    private final ActionPanel actionPanel = new ActionPanel();
    private final InteractablesPanel interactPanel = new InteractablesPanel();
    private final SocialisePanel socialisePanel = new SocialisePanel();
    private final LocationPanel locationPanel = new LocationPanel();

    @Override
    protected Screen getScreen() {
        return screen;
    }

    @Override
    public void update(GameEngine engine, double deltaTime) {

        SimCharacter player = engine.getActivePlayer();
        Location loc = player.getLocation();
        List<NPCCharacter> npcsHere = WorldRegistry.getInstance().getAllNPCs().stream()
                .filter(npc -> npc.getLocation().equals(loc))
                .collect(Collectors.toList());

        screen.getAttributePanel().setCharacter(
                player.getName(), player.getAge(),
                player.getMoney(), player.getNeeds());
        screen.getNpcPanel().setNPCs(
                loc.getLocationName(), npcsHere,
                player, engine.getRelationshipManager());
        interactPanel.setFurniture(loc.getFurniture());
        socialisePanel.setNPCs(npcsHere);

        if (!initialized) {
            transition(currentStep);
            dirty = true;
            initialized = true;
        }

        String input = InputQueue.poll();
        if (input == null) {
            return;
        }

        dirty = true;
        handleInput(input.trim(), engine);
    }
    private void transition(Step next) {
        currentStep = next;
        screen.setActionPanel(switch (next) {
            case MAIN ->
                actionPanel;
            case INTERACTABLES, INTERACTABLES_ACTION ->
                interactPanel;
            case SOCIALISE, SOCIALISE_ACTION ->
                socialisePanel;
            case CHANGE_LOCATION ->
                locationPanel;
        });
    }

    @Override
    public void handleInput(String input, GameEngine engine) {
        SimCharacter player = engine.getActivePlayer();
        Location loc = player.getLocation();
        List<NPCCharacter> npcsHere = WorldRegistry.getInstance().getAllNPCs().stream()
                .filter(npc -> npc.getLocation().equals(loc))
                .toList();

        switch (currentStep) {
            case MAIN -> {
                switch (input) {
                    case "1" ->
                        transition(Step.INTERACTABLES);
                    case "2" ->
                        transition(Step.SOCIALISE);
                    case "3" ->
                        transition(Step.CHANGE_LOCATION);
                    case "4" ->
                        engine.end();
                }
            }
            case INTERACTABLES -> {
                if (input.equals("0")) {
                    transition(Step.MAIN);
                } else {
                    List<Furniture> furniture = loc.getFurniture();
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < furniture.size()) {
                            selectedFurniture = furniture.get(idx);
                            interactPanel.selectFurniture(selectedFurniture);
                            currentStep = Step.INTERACTABLES_ACTION;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            case INTERACTABLES_ACTION -> {
                if (input.equals("0")) {
                    interactPanel.clearSelection();
                    currentStep = Step.INTERACTABLES;
                } else {
                    List<String> actions = selectedFurniture.getActionNames();
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < actions.size()) {
                            selectedFurniture.performAction(actions.get(idx), player);
                            interactPanel.clearSelection();
                            selectedFurniture = null;
                            transition(Step.MAIN);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            case SOCIALISE -> {
                if (input.equals("0")) {
                    transition(Step.MAIN);
                } else {
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < npcsHere.size()) {
                            selectedNPC = npcsHere.get(idx);
                            socialisePanel.selectNPC(selectedNPC);
                            currentStep = Step.SOCIALISE_ACTION;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            case SOCIALISE_ACTION -> {
                if (input.equals("0")) {
                    socialisePanel.clearSelection();
                    selectedNPC = null;
                    currentStep = Step.SOCIALISE;
                } else {
                    InteractionType[] types = InteractionType.values();
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < types.length) {
                            engine.getRelationshipManager()
                                    .interact(player, selectedNPC, types[idx]);
                            socialisePanel.clearSelection();
                            selectedNPC = null;
                            transition(Step.MAIN);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            case CHANGE_LOCATION -> {
                if (input.equals("0")) {
                    transition(Step.MAIN);
                } else {
                    List<Location> locations = new ArrayList<>(
                            WorldRegistry.getInstance().getAllLocations());
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        if (idx >= 0 && idx < locations.size()) {
                            player.setLocation(locations.get(idx));
                            transition(Step.MAIN);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
    }
}
