package ui.state;

import core.GameEngine;
import core.InputQueue;
import core.WorldRegistry;
import java.util.List;
import models.Career;
import models.CareerList;
import models.Location;
import models.SimCharacter;
import ui.layout.ScreenLayout;
import ui.panel.CreateSimPanel;
import ui.screen.CreateSimScreen;
import ui.screen.Screen;

public class CreateSimState extends BaseState<List<String>> {

    private final CreateSimScreen screen = new CreateSimScreen();

   private enum Step {
        NAME,
        AGE,
        GENDER,
        CONFIRM
    }
    private Step currentStep = Step.NAME;
    private String name = "", age = "", gender = "";

    @Override
    protected Screen getScreen() {
        return screen;
    }
    
    @Override
    public void update(GameEngine engine, double deltaTime) {

        String input = InputQueue.poll();
        if (input == null)
            return;
        dirty = true;
        CreateSimPanel panel = screen.getPanel();
        ScreenLayout layout = screen.getLayout();

        switch (currentStep) {
            case NAME -> {
                name = input;
                panel.setName(name);
                currentStep = Step.AGE;
                layout.setInputMode(ScreenLayout.InputMode.REQUEST);
            }
            case AGE -> {
                age = input;
                panel.setAge(age);
                currentStep = Step.GENDER;
                layout.setInputMode(ScreenLayout.InputMode.REQUEST);
            }
            case GENDER -> {
                gender = input;
                panel.setGender(gender);
                currentStep = Step.CONFIRM;
                layout.setInputMode(ScreenLayout.InputMode.CONFIRM);
            }
            case CONFIRM -> {
                if (input.equalsIgnoreCase("y")) {
                    handleInput(List.of(name, age, gender), engine);
                } else {
                    // Reset
                    currentStep = Step.NAME;
                    name = "";
                    age = "";
                    gender = "";
                    panel.setName("");
                    panel.setAge("");
                    panel.setGender("");
                    layout.setInputMode(ScreenLayout.InputMode.REQUEST);
                }
            }
        }
    }


    @Override
    public void handleInput(List<String> input, GameEngine engine) {

        String name = input.get(0);
        int age = Integer.parseInt(input.get(1));
        String gender = input.get(2);
        Location defaultLocation = WorldRegistry.getInstance().getLocation("Home");
        Career startingCareer = new Career(CareerList.JOBLESS);

        SimCharacter player = new SimCharacter(name, age, gender, defaultLocation, startingCareer);
        engine.getRelationshipManager().registerNewSim(player, engine.getSims(), WorldRegistry.getInstance().getAllNPCs());
        engine.setActivePlayer(player);

        engine.setGameState(new MainState());
    }
}
