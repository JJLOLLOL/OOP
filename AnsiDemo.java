public class AnsiDemo {

    // ANSI escape codes
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String DIM    = "\u001B[2m";

    private static final String BLACK  = "\u001B[30m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";
    private static final String MAGENTA= "\u001B[35m";
    private static final String CYAN   = "\u001B[36m";
    private static final String WHITE  = "\u001B[37m";

    private static final String GRAY   = "\u001B[90m";

    // Clear screen + move cursor home
    private static void clearScreen() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }

    private static String colorForPercent(int p) {
        if (p <= 20) return RED;
        if (p <= 50) return YELLOW;
        return GREEN;
    }

    private static String bar(int percent) {
        int total = 10;
        int filled = (int) Math.round((percent / 100.0) * total);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < total; i++) sb.append(i < filled ? "#" : "-");
        sb.append("] ");
        sb.append(String.format("%3d%%", percent));
        return sb.toString();
    }

    private static void printMenu(String simName, String status, String location,
                                  double money, String mood,
                                  int hunger, int energy, int hygiene, int bladder, int social, int fun) {

        clearScreen();


System.out.println("┌───────────────┬──────────────────────────────────────────────────────┐");
System.out.println("│ " + CYAN + "Day 0 | 08:00" + RESET +
                   " | " + BOLD + "SIM: John Doe (21M)" + RESET +
                   "        " + CYAN + "Status: Walking" + RESET +
                   "           │");
System.out.println("├───────────────┴─────────────┬────────────────────────────────────────┤");

System.out.println("│ " + BLUE + "Location : Park" + RESET +
                   "             │ " + BOLD + "OPTIONS" + RESET +
                   "                                │");

System.out.println("│ " + YELLOW + "Money    : $0.00" + RESET +
                   "            ├────────────────────────────────────────┤");

System.out.println("│ Mood     : Good             │ [1] Walk                               │");
System.out.println("│                             │ [2] Socialise                          │");

System.out.println("│ Hunger   " + GREEN + "[##########] 100%" + RESET +
                   "  │ [3] Relax                              │");

System.out.println("│ Energy   " + GREEN + "[##########] 100%" + RESET +
                   "  │ [4] Exercise                           │");

System.out.println("│ Hygiene  " + GREEN + "[##########] 100%" + RESET +
                   "  │ [5] Locations                          │");

System.out.println("│ Bladder  " + GREEN + "[##########] 100%" + RESET +
                   "  │ [6] Create SIM                         │");

System.out.println("│ Social   " + GREEN + "[##########] 100%" + RESET +
                   "  │ [7] Exit Game                          │");

System.out.println("│ Fun      " + GREEN + "[##########] 100%" + RESET +
                   "  │                                        │");

System.out.println("└─────────────────────────────┴────────────────────────────────────────┘");
System.out.print(GRAY + "> Select option: " + RESET);

        System.out.flush();
    }

    public static void main(String[] args) {
        printMenu(
                "John Doe",
                "Idle",
                "Home",
                0.00,
                "Good",
                100, 100, 100, 100, 100, 100
        );
    }
}
