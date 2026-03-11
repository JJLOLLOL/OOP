package states;

import core.GameEngine;
import core.GameState;
import ui.screens.CreationScreen;

public class CreationState implements GameState {

    private GameEngine engine;
    private CreationScreen screen;

    private enum Step {
        NAME,
        AGE,
        GENDER,
        CONFIRM,
        DONE
    }

    private Step step = Step.NAME;

    private String name = "";
    private int age = -1;
    private String gender = "";

    private String error = "";

    public CreationState(GameEngine engine) {

        this.engine = engine;
        this.screen = new CreationScreen();

    }

    @Override
    public void update() {

        if (step == Step.DONE) {

            System.out.println();
            System.out.println("Character Created Successfully.");
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Gender: " + gender);

            System.exit(0);

        }

    }

    @Override
    public void render() {

        screen.render(step, name, age, gender, error);

    }

    @Override
    public void handleInput(String input) {

        error = "";

        switch (step) {

            case NAME:

                if (input.trim().isEmpty()) {

                    error = "Name cannot be empty.";

                } else {

                    name = input.trim();
                    step = Step.AGE;

                }

                break;

            case AGE:

                try {

                    int value = Integer.parseInt(input);

                    if (value < 0 || value > 120) {

                        error = "Age must be between 0 and 120.";

                    } else {

                        age = value;
                        step = Step.GENDER;

                    }

                } catch (NumberFormatException e) {

                    error = "Age must be a number.";

                }

                break;

            case GENDER:

                input = input.toUpperCase();

                if (input.equals("M") || input.equals("F")) {

                    gender = input;
                    step = Step.CONFIRM;

                } else {

                    error = "Enter M or F.";

                }

                break;

            case CONFIRM:

                if (input.equalsIgnoreCase("Y")) {

                    step = Step.DONE;

                } else if (input.equalsIgnoreCase("N")) {

                    step = Step.NAME;
                    name = "";
                    age = -1;
                    gender = "";

                } else {

                    error = "Enter Y or N.";

                }

                break;

        }

    }

}