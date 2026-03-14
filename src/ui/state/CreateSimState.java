package ui.state;

import core.GameEngine;
import core.WorldRegistry;
import java.util.ArrayList;
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

        while (true) {

            int simCount = askSimCount(layout, scanner);
            panel.setMaxSims(simCount);
            panel.reset();

            List<SimCharacter> sims = new ArrayList<>();

            for (int i = 1; i <= simCount; i++) {

                layout.setInputMode(ScreenLayout.InputMode.REQUEST);

                screen.render();
                String name = layout.readField("Sim " + i + " Name", scanner);
                panel.setName(name);

                screen.render();
                String ageStr = layout.readField("Age", scanner);
                panel.setAge(ageStr);

                screen.render();
                String gender = layout.readField("Gender", scanner);
                panel.setGender(gender);

                int age = Integer.parseInt(ageStr);

                panel.commitSim();
                screen.render();

                Location defaultLocation = WorldRegistry.getInstance().getLocation("Home");
                Career startingCareer = new Career(CareerList.JOBLESS);

                sims.add(new SimCharacter(name, age, gender, defaultLocation, startingCareer));
            }
            panel.showOptions(true);
            screen.render();

            while (true) {

                String input = layout.readField("Option", scanner);

                switch (input) {

                    case "1":
                        engine.setActivePlayer(sims.get(0));
                        engine.setGameState(new MainState());
                        return;

                    case "2":
                        editCharacter(layout, scanner, sims);
                        break;

                    case "3":
                        sims.clear();
                        panel.reset();
                        break;

                    default:
                        System.out.println("Invalid option.");
                        continue;
                }

                if (input.equals("3"))
                    break;
            }
        }
    }

    @Override
    public void handleInput(List<String> input, GameEngine engine) {
        // No longer used (logic moved into render loop)
    }

    private int askSimCount(ScreenLayout layout, Scanner scanner) {

        while (true) {

            System.out.print("How many Sims do you want to create (1-4): ");

            try {
                int count = Integer.parseInt(scanner.nextLine());

                if (count >= 1 && count <= 4)
                    return count;

            } catch (Exception ignored) {
            }

            System.out.println("Please enter a number between 1 and 4.");
        }
    }

    private void editCharacter(
            ScreenLayout layout,
            Scanner scanner,
            List<SimCharacter> sims) {

        System.out.print("Which character to edit (1-" + sims.size() + "): ");

        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= sims.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        screen.render();
        String name = layout.readField("Name", scanner);

        screen.render();
        String ageStr = layout.readField("Age", scanner);

        screen.render();
        String gender = layout.readField("Gender", scanner);

        int age = Integer.parseInt(ageStr);

        Location defaultLocation = WorldRegistry.getInstance().getLocation("Home");

        Career startingCareer = new Career(CareerList.JOBLESS);

        sims.set(index,
                new SimCharacter(name, age, gender, defaultLocation, startingCareer));
    }
}