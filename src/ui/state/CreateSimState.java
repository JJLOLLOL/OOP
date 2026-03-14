package ui.state;

import core.GameEngine;
import core.WorldRegistry;
import java.util.List;
import java.util.Scanner;
import models.Career;
import models.CareerList;
import models.Location;
import models.SimCharacter;
import ui.layout.ScreenLayout;
import ui.panel.CreateSimPanel;
import ui.screen.CreateSimScreen;

public class CreateSimState implements State<List<String>> {

    private final CreateSimScreen screen = new CreateSimScreen();

    @Override
    public void render(GameEngine engine) {

        Scanner scanner = engine.getScanner();
        ScreenLayout layout = screen.getLayout();
        CreateSimPanel panel = screen.getPanel();

        layout.setInputMode(ScreenLayout.InputMode.REQUEST);
        screen.render();
        String name = layout.readField("Name", scanner);
        panel.setName(name);
        screen.render();
        String age = layout.readField("Age", scanner);
        panel.setAge(age);
        screen.render();
        String gender = layout.readField("Gender", scanner);
        panel.setGender(gender);

        layout.setInputMode(ScreenLayout.InputMode.CONFIRM);
        screen.render();
        Boolean confirmed = layout.readConfirm(scanner);
        if (confirmed) handleInput(List.of(name, age, gender), engine);

    }

    @Override
    public void handleInput(List<String> input, GameEngine engine) {

        String name = input.get(0);
        int age = Integer.parseInt(input.get(1));
        String gender = input.get(2);
        Location DefaultLocation = WorldRegistry.getInstance().getLocation("Home");
        Career startingCareer = new Career(CareerList.JOBLESS);

        SimCharacter player = new SimCharacter(name, age, gender, DefaultLocation, startingCareer);
        engine.setActivePlayer(player);
        
        engine.setGameState(new MainState());
    }
}
