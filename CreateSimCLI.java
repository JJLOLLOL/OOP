import java.util.Scanner;

public class CreateSimCLI {

    // ANSI codes
    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";
    static final String CYAN   = "\u001B[36m";
    static final String BLUE   = "\u001B[34m";
    static final String YELLOW = "\u001B[33m";
    static final String GRAY   = "\u001B[90m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String name = "??";
        String age = "--";
        String gender = "--";

        // Step 1: Name
        clearScreen();
        printCreateSimBox(name, age, gender);
        System.out.print(GRAY + "> Enter Name: " + RESET);
        name = sc.nextLine();

        // Step 2: Age
        clearScreen();
        printCreateSimBox(name, age, gender);
        System.out.print(GRAY + "> Enter Age: " + RESET);
        age = sc.nextLine();

        // Step 3: Gender
        clearScreen();
        printCreateSimBox(name, age, gender);
        System.out.print(GRAY + "> Enter Gender (M/F): " + RESET);
        gender = sc.nextLine();

        // Final screen
        clearScreen();
        printCreateSimBox(name, age, gender);

System.out.println("┌──────────────────────────────────────────────┐");
System.out.println("│ " + BOLD + CYAN + "           WELCOME TO THE SIMS" + RESET +
                   "               │");
System.out.println("├──────────────────────────────────────────────┤");
System.out.println("│ " + BOLD + "Create Your SIM Character" + RESET +
                   "                    │");
System.out.println("│                                              │");
System.out.println("│ " + BLUE + "Name   :" + RESET + " John Doe                            │");
System.out.println("│ " + BLUE + "Age    :" + RESET + " 21                                  │");
System.out.println("│ " + BLUE + "Gender :" + RESET + " Male                                │");
System.out.println("│                                              │");
System.out.println("├──────────────────────────────────────────────┤");
System.out.println("│ [1] Confirm & Start Game                     │");
System.out.println("│ [2] Edit Details                             │");
System.out.println("│ [3] Exit Game                                │");
System.out.println("└──────────────────────────────────────────────┘");
System.out.print(GRAY + "> Select option: " + RESET);

    }

    // Clear terminal screen
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // ANSI-colored creation box
    private static void printCreateSimBox(String name, String age, String gender) {
        System.out.println("┌──────────────────────────────────────────────┐");
        System.out.println("│ " + BOLD + CYAN + "           WELCOME TO THE SIMS" + RESET +
                           "               │");
        System.out.println("├──────────────────────────────────────────────┤");
        System.out.println("│ " + BOLD + "Create Your SIM Character" + RESET +
                           "                    │");
        System.out.println("│                                              │");
        System.out.printf ("│ " + BLUE + "Name   :" + RESET + " %-34s  │%n", name);
        System.out.printf ("│ " + BLUE + "Age    :" + RESET + " %-34s  │%n", age);
        System.out.printf ("│ " + BLUE + "Gender :" + RESET + " %-34s  │%n", gender);
        System.out.println("│                                              │");
        System.out.println("└──────────────────────────────────────────────┘");
    }
}
