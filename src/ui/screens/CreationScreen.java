package ui.screens;

public class CreationScreen {

    public void render(Enum<?> step, String name, int age, String gender, String error) {

        System.out.println("Create Your Character");
        System.out.println("---------------------");

        System.out.println("Name: " + name);
        System.out.println("Age: " + (age == -1 ? "" : age));
        System.out.println("Gender: " + gender);

        System.out.println();

        if (!error.isEmpty()) {
            System.out.println("Error: " + error);
            System.out.println();
        }

        switch (step.toString()) {

            case "NAME":
                System.out.print("Enter name: ");
                break;

            case "AGE":
                System.out.print("Enter age: ");
                break;

            case "GENDER":
                System.out.print("Enter gender (M/F): ");
                break;

            case "CONFIRM":
                System.out.print("Confirm character? (Y/N): ");
                break;

        }

    }

}