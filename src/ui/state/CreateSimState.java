package ui.state;

import core.GameEngine;
import java.util.List;
import java.util.Scanner;
import ui.layout.ScreenLayout;
import ui.panel.CreateSimPanel;
import ui.screen.CreateSimScreen;

public class CreateSimState implements State<List<String>> {

    private final Scanner scanner = new Scanner(System.in);
    private final CreateSimScreen screen = new CreateSimScreen();

    @Override
    public void render(GameEngine engine) {
        ScreenLayout layout = screen.getLayout();
        CreateSimPanel panel = screen.getPanel();
        screen.render();
        String name = layout.readField("Name", scanner);
        panel.setName(name);
        screen.render();
        String age = layout.readField("Age", scanner);
        panel.setAge(age);
        screen.render();
        String gender = layout.readField("Gender", scanner);
        panel.setGender(gender);
        handleInput(List.of(name, age, gender), engine);
    }

    @Override
    public void handleInput(List<String> input, GameEngine engine) {
        String name = input.get(0);
        int age = Integer.parseInt(input.get(1));
        String gender = input.get(2);
    }
}
