package controller.creation;

import types.Gender;
import controller.CreateSimController;
import ui.Renderer;

public class GenderStepHandler implements CreationStepHandler {

    @Override
    public boolean handleInput(String input, CreateSimController context) {
        try {
            Gender gender = Gender.fromUserInput(input);
            context.getCurrentBuilder().withGender(gender);
            context.advanceToNextBuilder();

            if (context.isCreationFinished()) {
                context.setStepHandler(new ConfirmStepHandler());
            } else {
                context.setStepHandler(new NameStepHandler());
            }
        } catch (IllegalArgumentException e) {
            Renderer.showError("Enter M for Male or F for Female.");
            return false;
        }
        return true;
    }

    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.GENDER;
    }
}