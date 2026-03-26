package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

public class AgeStepHandler implements CreationStepHandler {

    @Override
    public boolean handleInput(String input, CreateSimController context) {
        try {
            int age = Integer.parseInt(input);
            if (age < 10 || age > 90) {
                throw new NumberFormatException();
            }
            context.getCurrentBuilder().withAge(age);
            context.setStepHandler(new GenderStepHandler());
        } catch (NumberFormatException e) {
            Renderer.showError("Age must be a number between 10 and 90.");
            return false;
        }
        return true;
    }

    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.AGE;
    }
}