package ui.views;

import controller.CreateSimController;
import core.GameState;
import controller.creation.SimCharacterBuilder;
import models.character.SimCharacter;

import java.util.List;

import static ui.ConsoleUtils.*;
import static ui.Renderer.*;

/**
 * Renders the user interface for the Sim creation phase.
 */
public class CreateSimView {

    /**
     * Renders the Sim-creation wizard screen.
     *
     * @param state the current {@link GameState}
     */
    public static void render(GameState state, CreateSimController controller) {
        printBanner("CREATE YOUR SIMS");
        System.out.println();
        switch (controller.getStep()) {
            case COUNT ->
                prompt("How many Sims do you want to create?");
            case NAME -> {
                showCommitted(controller.getCommitted());
                System.out.printf("  " + MUTED + "Creating Sim %d of %d%n" + RESET,
                        controller.getCurrentIndex() + 1, controller.getTotalSims());
                prompt("Enter name:");
            }
            case AGE -> {
                showCommitted(controller.getCommitted());
                field("Name", controller.getInFlightName());
                prompt("Enter age:");
            }
            case GENDER -> {
                showCommitted(controller.getCommitted());
                field("Name", controller.getInFlightName());
                field("Age", controller.getInFlightAge());
                prompt("Enter gender (M / F):");
            }
            case CONFIRM -> {
                System.out.println("  " + TITLE + "Review your Sims:" + RESET + "\n");
                List<SimCharacterBuilder> committed = controller.getCommitted();
                for (int i = 0; i < committed.size(); i++) {
                    SimCharacterBuilder builder = committed.get(i);
                    System.out.printf("    " + BRIGHT_YELLOW + "%d. " + RESET + BRIGHT_WHITE + "%s" + RESET + "%n", i + 1, simLabel(builder.getName(), String.valueOf(builder.getAge()), builder.getGenderLabel()));
                }
                System.out.println("\n  " + LABEL + "Confirm? " + RESET + BRIGHT_GREEN + "(Y)" + RESET + " / " + BRIGHT_RED + "(N)" + RESET);
            }
            case PICK_PLAYER -> {
                System.out.println("  " + TITLE + "Choose your active Sim:" + RESET + "\n");
                for (int i = 0; i < state.getSims().size(); i++) {
                    SimCharacter s = state.getSims().get(i);
                    System.out.printf("    " + BRIGHT_YELLOW + "%d. " + RESET + BRIGHT_WHITE + "%s" + RESET + "%n", i + 1, simLabel(s.getName(), String.valueOf(s.getAge()), s.getGender().getLabel()));
                }
            }
        }
        System.out.print("\n> ");
    }

    private static void prompt(String text) {
        System.out.println("  " + LABEL + text + RESET);
    }

    private static void field(String key, String val) {
        System.out.println("  " + MUTED + pad(key, 4) + " : " + RESET + BRIGHT_WHITE + val + RESET);
    }

    private static void printBanner(String title) {
        System.out.println(BORDER + "┌" + seg(INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + CLOCK + center(title, INNER_W) + RESET + BORDER + "│" + RESET);
        System.out.println(BORDER + "└" + seg(INNER_W) + "┘" + RESET);
    }

    private static String simLabel(String name, String age, String gender) {
        return BRIGHT_WHITE + name + RESET + MUTED + " (" + age + gender + ")" + RESET;
    }

    private static void showCommitted(List<SimCharacterBuilder> committed) {
        if (committed.isEmpty()) {
            return;
        }
        System.out.println("  " + MUTED + "Sims added so far:" + RESET);
        for (SimCharacterBuilder builder : committed) {
            // Only show builders that have been fully defined (i.e., have a gender)
            String genderLabel = builder.getGenderLabel();
            if (genderLabel != null && !genderLabel.isEmpty()) {
                System.out.println("    " + BRIGHT_BLACK + "•" + RESET + " " + simLabel(builder.getName(), String.valueOf(builder.getAge()), genderLabel));
            }
        }
        System.out.println();
    }
}