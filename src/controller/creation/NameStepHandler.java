package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

public class NameStepHandler implements CreationStepHandler {

    @Override
    public boolean handleInput(String input, CreateSimController context) {
        if (input.isBlank()) {
            Renderer.showError("Name cannot be empty.");
            return false;
        }
        context.getCurrentBuilder().withName(input);
        context.setStepHandler(new AgeStepHandler());
        return true;
    }

    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.NAME;
    }
}