package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

public class AgeStepHandler implements CreationStepHandler {

    private final int MAX_AGE = 90;
    private final int MIN_AGE = 10;

    @Override
    public boolean handleInput(String input, CreateSimController context) {
        try {
            int age = Integer.parseInt(input);
            if (age < MIN_AGE || age > MAX_AGE) {
                throw new NumberFormatException();
            }
            context.getCurrentBuilder().withAge(age);
            context.setStepHandler(new GenderStepHandler());
        } catch (NumberFormatException e) {
            Renderer.showError(String.format("Age must be a number between %d and %d.", MIN_AGE, MAX_AGE));
            return false;
        }
        return true;
    }

    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.AGE;
    }
}