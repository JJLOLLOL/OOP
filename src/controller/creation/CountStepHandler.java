package controller.creation;

import controller.CreateSimController;
import ui.Renderer;

public class CountStepHandler implements CreationStepHandler {

    private static final int MAX_SIMS = 5;

    @Override
    public boolean handleInput(String input, CreateSimController context) {
        try {
            int n = Integer.parseInt(input);
            if (n < 1 || n > MAX_SIMS) {
                throw new NumberFormatException();
            }
            context.initializeBuilders(n);
            context.setStepHandler(new NameStepHandler());
        } catch (NumberFormatException e) {
            Renderer.showError("Enter a number between 1 and " + MAX_SIMS + ".");
            return false;
        }
        return true;
    }

    @Override
    public CreateSimController.Step getStep() {
        return CreateSimController.Step.COUNT;
    }
}