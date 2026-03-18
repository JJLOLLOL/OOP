package ui;

import Types.CareerList;
import Types.InteractionType;
import core.CreateSimController;
import core.GameState;
import core.PlayController;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.Location;
import models.SimCharacter;
import models.actions.Furniture;
import models.needs.Need;

/**
 * Single source of truth for all terminal output in the game.
 *
 * <p>
 * Nothing outside this class should call {@code System.out}. All rendering is
 * driven by {@link #render(GameState, WorldRegistry)}, which dispatches to the
 * correct phase renderer based on {@link GameState.Phase}.
 *
 * <h3>Colour scheme</h3>
 * <pre>
 *  Borders / labels      → BRIGHT_BLACK (dark gray)
 *  Panel titles          → BOLD + BRIGHT_CYAN
 *  Clock header          → BOLD + BRIGHT_WHITE
 *  Sim name              → BOLD + BRIGHT_WHITE
 *  Menu numbers          → BRIGHT_YELLOW
 *  Menu labels           → WHITE
 *  Back item             → BRIGHT_BLACK
 *  Money                 → BRIGHT_YELLOW
 *  Location name         → BRIGHT_CYAN
 *  Need bars (high)      → BRIGHT_GREEN
 *  Need bars (mid)       → BRIGHT_YELLOW
 *  Need bars (low)       → BRIGHT_RED
 *  Skill bars            → BRIGHT_BLACK labels, colour-coded fill
 *  Relationship positive → BRIGHT_GREEN
 *  Relationship negative → BRIGHT_RED
 *  Relationship neutral  → BRIGHT_YELLOW
 *  Notifications pos     → BRIGHT_GREEN
 *  Notifications neg     → BRIGHT_RED
 *  Notifications warn    → BRIGHT_YELLOW
 *  Notifications neutral → BRIGHT_WHITE
 *  Error prompt          → BRIGHT_RED
 *  Career info           → MAGENTA / BRIGHT_MAGENTA
 * </pre>
 */
public class Renderer {

    // ── ANSI colour codes ─────────────────────────────────────────────────────
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String WHITE = "\u001B[37m";
    private static final String BRIGHT_BLACK = "\u001B[90m";
    private static final String BRIGHT_RED = "\u001B[91m";
    private static final String BRIGHT_GREEN = "\u001B[92m";
    private static final String BRIGHT_YELLOW = "\u001B[93m";
    private static final String BRIGHT_BLUE = "\u001B[94m";
    private static final String BRIGHT_MAGENTA = "\u001B[95m";
    private static final String BRIGHT_CYAN = "\u001B[96m";
    private static final String BRIGHT_WHITE = "\u001B[97m";

    // ── Semantic aliases (change the scheme here, not throughout the code) ────
    private static final String BORDER = BRIGHT_BLACK;   // box lines and dividers
    private static final String LABEL = BRIGHT_BLACK;   // dim labels like "Location:"
    private static final String MUTED = BRIGHT_BLACK;   // secondary / inactive text
    private static final String TITLE = BOLD + BRIGHT_CYAN;  // panel headings
    private static final String CLOCK = BOLD + BRIGHT_WHITE; // clock header
    private static final String SIM_NAME = BOLD + BRIGHT_WHITE; // sim name

    // ── Column widths ─────────────────────────────────────────────────────────
    private static final int COL_W = 30;
    private static final int LEFT_W = COL_W;
    private static final int MID_W = COL_W;
    private static final int SKILLS_W = COL_W;
    private static final int NOTIF_W = COL_W;
    private static final int INNER_W = 4 * (COL_W + 2) + 3;
    private static final int BAR_WIDTH = 10;

    // ══════════════════════════════════════════════════════════════════════════
    //  Public API
    // ══════════════════════════════════════════════════════════════════════════
    public static void render(GameState state, WorldRegistry world) {
        clearScreen();
        switch (state.getPhase()) {
            case CREATE_SIM ->
                renderCreateSim(state);
            case PLAYING ->
                renderPlaying(state, world);
            case QUIT -> {
                /* nothing */ }
        }
    }

    public static void showError(String message) {
        System.out.println("  " + BRIGHT_RED + "[!] " + RESET + WHITE + message + RESET);
        System.out.print("> ");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CREATE SIM phase
    // ══════════════════════════════════════════════════════════════════════════
    private static void renderCreateSim(GameState state) {
        printBanner("CREATE YOUR SIMS");
        System.out.println();

        switch (CreateSimController.getStep()) {

            case COUNT ->
                System.out.println("  " + LABEL + "How many Sims do you want to create?" + RESET);

            case NAME -> {
                showCommitted(CreateSimController.getCommitted());
                System.out.printf("  " + MUTED + "Creating Sim %d of %d%n" + RESET,
                        CreateSimController.getCurrentIndex() + 1,
                        CreateSimController.getTotalSims());
                System.out.println("  " + LABEL + "Enter name:" + RESET);
            }

            case AGE -> {
                showCommitted(CreateSimController.getCommitted());
                System.out.println("  " + MUTED + "Name : " + RESET
                        + BRIGHT_WHITE + CreateSimController.getInFlightName() + RESET);
                System.out.println("  " + LABEL + "Enter age:" + RESET);
            }

            case GENDER -> {
                showCommitted(CreateSimController.getCommitted());
                System.out.println("  " + MUTED + "Name : " + RESET
                        + BRIGHT_WHITE + CreateSimController.getInFlightName() + RESET);
                System.out.println("  " + MUTED + "Age  : " + RESET
                        + BRIGHT_WHITE + CreateSimController.getInFlightAge() + RESET);
                System.out.println("  " + LABEL + "Enter gender (M / F):" + RESET);
            }

            case CONFIRM -> {
                System.out.println("  " + TITLE + "Review your Sims:" + RESET + "\n");
                List<String[]> committed = CreateSimController.getCommitted();
                for (int i = 0; i < committed.size(); i++) {
                    String[] d = committed.get(i);
                    System.out.printf("    " + BRIGHT_YELLOW + "%d. " + RESET
                            + BRIGHT_WHITE + "%s" + RESET + "%n", i + 1, simLabel(d[0], d[1], d[2]));
                }
                System.out.println("\n  " + LABEL + "Confirm? " + RESET
                        + BRIGHT_GREEN + "(Y)" + RESET + " / " + BRIGHT_RED + "(N)" + RESET);
            }

            case PICK_PLAYER -> {
                System.out.println("  " + TITLE + "Choose your active Sim:" + RESET + "\n");
                for (int i = 0; i < state.getSims().size(); i++) {
                    SimCharacter s = state.getSims().get(i);
                    System.out.printf("    " + BRIGHT_YELLOW + "%d. " + RESET
                            + BRIGHT_WHITE + "%s" + RESET + "%n", i + 1,
                            simLabel(s.getName(), String.valueOf(s.getAge()), s.getGender()));
                }
            }
        }

        System.out.print("\n> ");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PLAYING phase
    // ══════════════════════════════════════════════════════════════════════════
    private static void renderPlaying(GameState state, WorldRegistry world) {
        SimCharacter player = state.getActivePlayer();
        Location loc = player.getLocation();
        PlayController.Step step = PlayController.getStep();

        List<String> stats = buildStatsPanel(player, loc, state, world);
        List<String> actions = buildActionsPanel(step, loc, player, state, world);
        List<String> skills = buildSkillsPanel(player);
        List<String> notifs = buildNotificationsPanel(player);

        printBoxTop(formatClock(state));
        printColumnSeparator();
        printBodyRows(stats, actions, skills, notifs);
        printBoxBottom();

        System.out.print("\n> ");
    }

    // ── Box drawing ───────────────────────────────────────────────────────────
    private static void printBoxTop(String clock) {
        System.out.println(BORDER + "┌" + repeat("─", INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + centerColoured(clock, INNER_W) + BORDER + "│" + RESET);
    }

    private static void printColumnSeparator() {
        System.out.println(BORDER + "├" + repeat("─", LEFT_W + 2)
                + "┬" + repeat("─", MID_W + 2)
                + "┬" + repeat("─", SKILLS_W + 2)
                + "┬" + repeat("─", NOTIF_W + 2) + "┤" + RESET);
    }

    private static void printBodyRows(List<String> left, List<String> mid,
            List<String> skills, List<String> notifs) {
        int rows = Math.max(Math.max(left.size(), mid.size()),
                Math.max(skills.size(), notifs.size()));
        for (int i = 0; i < rows; i++) {
            printRow(
                    i < left.size() ? left.get(i) : "",
                    i < mid.size() ? mid.get(i) : "",
                    i < skills.size() ? skills.get(i) : "",
                    i < notifs.size() ? notifs.get(i) : ""
            );
        }
    }

    private static void printBoxBottom() {
        System.out.println(BORDER + "└" + repeat("─", LEFT_W + 2)
                + "┴" + repeat("─", MID_W + 2)
                + "┴" + repeat("─", SKILLS_W + 2)
                + "┴" + repeat("─", NOTIF_W + 2) + "┘" + RESET);
    }

    private static void printRow(String l, String m, String s, String n) {
        System.out.println(
                BORDER + "│" + RESET + " " + padColoured(l, LEFT_W)
                + " " + BORDER + "│" + RESET + " " + padColoured(m, MID_W)
                + " " + BORDER + "│" + RESET + " " + padColoured(s, SKILLS_W)
                + " " + BORDER + "│" + RESET + " " + padColoured(n, NOTIF_W)
                + " " + BORDER + "│" + RESET
        );
    }

    // ── Panel builders ────────────────────────────────────────────────────────
    private static List<String> buildStatsPanel(SimCharacter player, Location loc,
            GameState state, WorldRegistry world) {
        List<String> lines = new ArrayList<>();

        // Sim name + age/gender
        lines.add(SIM_NAME + player.getName() + RESET
                + MUTED + " (" + player.getAge() + player.getGender().charAt(0) + ")" + RESET);

        // Career status under name
        models.Career career = player.getCareer();
        if (career.getCurrentCareer() != CareerList.JOBLESS) {
            lines.add(BRIGHT_MAGENTA + career.getTitle() + RESET
                    + MUTED + "  " + career.getRank() + RESET);
        } else {
            lines.add(MUTED + "Unemployed" + RESET);
        }

        lines.add(""); // breathing room

        // Need bars
        for (Map.Entry<String, Need> e : player.getNeeds().entrySet()) {
            lines.add(needBar(e.getValue()));
        }

        // Money
        lines.add("");
        lines.add(BRIGHT_YELLOW + "Money: $" + String.format("%.2f", player.getMoney()) + RESET);

        // Divider
        lines.add(BORDER + repeat("─", LEFT_W) + RESET);

        // Location
        lines.add(LABEL + "At " + RESET + BRIGHT_CYAN + loc.getLocationName() + RESET);

        // Nearby characters
        List<models.Character> chars = PlayController.charsAt(loc, state, world);
        if (chars.isEmpty()) {
            lines.add(MUTED + "No one nearby." + RESET);
        } else {
            lines.add(LABEL + "nearby:" + RESET);
            for (models.Character c : chars) {
                String status = state.getRelationshipService().getStatus(player, c);
                int score = state.getRelationshipService().getScore(player, c);
                String scoreColour = score > 0 ? BRIGHT_GREEN : score < 0 ? BRIGHT_RED : BRIGHT_YELLOW;
                lines.add(WHITE + c.getName() + RESET
                        + MUTED + " [" + status + "] " + RESET
                        + scoreColour + score + RESET);
            }
        }

        return lines;
    }

    private static List<String> buildActionsPanel(PlayController.Step step, Location loc,
            SimCharacter player, GameState state,
            WorldRegistry world) {
        List<String> lines = new ArrayList<>();

        switch (step) {

            case MAIN -> {
                lines.add(menuTitle("Actions"));
                lines.add(menuItem("1", "Interact Objects"));
                lines.add(menuItem("2", "Socialise"));
                lines.add(menuItem("3", "Change Location"));
                lines.add(menuItem("4", "Switch Character"));
                lines.add(menuItem("5", "Exit Game"));
            }

            case INTERACTABLES -> {
                lines.add(menuTitle("Interact Objects"));
                List<Furniture> flist = loc.getFurnitures();
                for (int i = 0; i < flist.size(); i++) {
                    lines.add(menuItem(String.valueOf(i + 1), flist.get(i).getName()));
                }
                lines.add(backItem());
            }

            case INTERACTABLE_ACTION -> {
                Furniture f = PlayController.getSelectedFurniture();
                lines.add(menuTitle(f.getName()));
                List<String> acts = new ArrayList<>(f.getActionNames());
                for (int i = 0; i < acts.size(); i++) {
                    lines.add(menuItem(String.valueOf(i + 1), acts.get(i)));
                }
                lines.add(backItem());
            }

            case SOCIALISE -> {
                lines.add(menuTitle("Socialise"));
                List<models.Character> chars = PlayController.charsAt(loc, state, world);
                if (chars.isEmpty()) {
                    lines.add(MUTED + "Nobody here." + RESET);
                } else {
                    for (int i = 0; i < chars.size(); i++) {
                        models.Character c = chars.get(i);
                        String status = state.getRelationshipService().getStatus(player, c);
                        lines.add(menuItem(String.valueOf(i + 1),
                                c.getName() + " " + MUTED + "[" + status + "]" + RESET));
                    }
                }
                lines.add(backItem());
            }

            case SOCIALISE_ACTION -> {
                models.Character target = PlayController.getSelectedCharacter();
                lines.add(menuTitle("Interact: " + target.getName()));
                InteractionType[] types = InteractionType.values();
                for (int i = 0; i < types.length; i++) {
                    lines.add(menuItem(String.valueOf(i + 1), types[i].getLabel()));
                }
                lines.add(backItem());
            }

            case CHANGE_LOCATION -> {
                lines.add(menuTitle("Go to..."));
                List<Location> locs = new ArrayList<>(world.getAllLocations());
                for (int i = 0; i < locs.size(); i++) {
                    boolean here = locs.get(i).equals(loc);
                    String label = locs.get(i).getLocationName()
                            + (here ? " " + BRIGHT_CYAN + "← here" + RESET : "");
                    lines.add(menuItem(String.valueOf(i + 1), label));
                }
                lines.add(backItem());
            }

            case SWITCH_CHARACTER -> {
                lines.add(menuTitle("Switch Sim"));
                List<SimCharacter> sims = state.getSims();
                for (int i = 0; i < sims.size(); i++) {
                    boolean active = sims.get(i).equals(player);
                    String label = sims.get(i).getName()
                            + (active ? " " + BRIGHT_GREEN + "← active" + RESET : "");
                    lines.add(menuItem(String.valueOf(i + 1), label));
                }
                lines.add(backItem());
            }

            case PICK_CAREER -> {
                lines.add(menuTitle("Choose Career"));

                List<CareerList> careers = PlayController.getAvailableCareers();

                for (int i = 0; i < careers.size(); i++) {
                    CareerList c = careers.get(i);
                    int TITLE_W = 18;
                    int SALARY_W = 10;
                    int HOURS_W = 4;
                    String title = pad(c.getTitle(), TITLE_W);
                    String salary = String.format("$%.0f/d", c.getBaseSalary());
                    salary = pad(salary, SALARY_W);
                    String hours = c.getWorkingHours() > 0 ? String.format("%dh", (int) c.getWorkingHours()): "";
                    hours = pad(hours, HOURS_W);
                    String row = BRIGHT_YELLOW + (i + 1) + ". " + RESET + BRIGHT_WHITE + title + RESET + MUTED + " " + salary + " " + hours + RESET;
                    lines.add(row);
                }
                lines.add(backItem());
            }
        }

        return lines;
    }

    private static List<String> buildSkillsPanel(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(menuTitle("Skills"));
        for (Map.Entry<String, models.Skills> e : player.getAllSkills().entrySet()) {
            lines.add(skillBar(e.getValue()));
        }
        return lines;
    }

    private static List<String> buildNotificationsPanel(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(menuTitle("Notifications"));

        List<String> notes = player.getNotifications();
        if (notes.isEmpty()) {
            lines.add(MUTED + "None." + RESET);
            return lines;
        }

        for (String note : notes) {
            String colour = classifyNotification(note);
            for (String segment : note.split("\n")) {
                String clean = segment.trim();
                if (clean.isEmpty()) {
                    continue;
                }
                for (String line : wordWrap(clean, NOTIF_W)) {
                    lines.add(colour + line + RESET);
                }
            }
            lines.add("");
        }

        while (!lines.isEmpty() && stripAnsi(lines.get(lines.size() - 1)).isBlank()) {
            lines.remove(lines.size() - 1);
        }

        return lines;
    }

    // ── Notification classifier ───────────────────────────────────────────────
    private static String classifyNotification(String note) {
        String lower = note.toLowerCase();
        if (lower.contains("levelled up") || lower.contains("improved")
                || lower.contains("unlocked") || lower.contains("gained")
                || lower.contains("promoted") || lower.contains("earned")
                || lower.contains("started career")) {
            return BRIGHT_GREEN;
        }
        if (lower.contains("failed") || lower.contains("worsened")
                || lower.contains("starving") || lower.contains("exhausted")
                || lower.contains("critically") || lower.contains("can't")
                || lower.contains("cannot") || lower.contains("not enough")) {
            return BRIGHT_RED;
        }
        if (lower.contains("warning") || lower.contains("low")
                || lower.contains("late") || lower.contains("lonely")
                || lower.contains("bored") || lower.contains("dirty")
                || lower.contains("unchanged") || lower.contains("cost")) {
            return BRIGHT_YELLOW;
        }
        return BRIGHT_WHITE;
    }

    // ── Need bar ─────────────────────────────────────────────────────────────
    private static String needBar(Need need) {
        int val = (int) need.getValue();
        int filled = val * BAR_WIDTH / 100;
        int empty = BAR_WIDTH - filled;
        String colour = val >= 70 ? BRIGHT_GREEN : val >= 40 ? BRIGHT_YELLOW : BRIGHT_RED;

        String label = LABEL + String.format("%-8s", need.getNeedName()) + RESET;
        String bar = MUTED + "[" + RESET
                + colour + repeat("#", filled) + RESET
                + MUTED + repeat("-", empty) + RESET
                + MUTED + "]" + RESET;
        String valueStr = colour + String.format("%3d", val) +  "%" + RESET;

        return label + " " + bar + " " + valueStr;
    }

    // ── Skill bar ─────────────────────────────────────────────────────────────
    private static String skillBar(models.Skills skill) {
        int pct = (int) Math.min(100, (skill.getProgress() / skill.getRequiredXP()) * 100);
        int filled = pct * BAR_WIDTH / 100;
        int empty = BAR_WIDTH - filled;
        String colour = pct >= 70 ? BRIGHT_GREEN : pct >= 40 ? BRIGHT_YELLOW : BRIGHT_BLUE;

        // Skills use BLUE at the low end instead of RED — not being bad at a skill
        // is different from a need being critically low.
        String label = LABEL + String.format("%-11s", skill.getSkillName()) + RESET;
        String bar = MUTED + "[" + RESET
                + colour + repeat("#", filled) + RESET
                + MUTED + repeat("-", empty) + RESET
                + MUTED + "]" + RESET;
        String level = MUTED + "Lv" + (int) skill.getLevel() + RESET;

        return label + " " + bar + " " + level;
    }

    // ── Menu helpers ──────────────────────────────────────────────────────────
    private static String menuTitle(String title) {
        return TITLE + title + RESET;
    }

    private static String menuItem(String num, String label) {
        return BRIGHT_YELLOW + num + "." + RESET + " " + WHITE + label + RESET;
    }

    private static String backItem() {
        return MUTED + "0. Back" + RESET;
    }

    // ── Clock & banner ────────────────────────────────────────────────────────
    private static String formatClock(GameState state) {
        return CLOCK + "DAY " + state.getGameClock().getDays()
                + "  ─  " + String.format("%02d:%02d",
                        state.getGameClock().getHours(),
                        state.getGameClock().getMinutes()) + RESET;
    }

    private static void printBanner(String title) {
        System.out.println(BORDER + "┌" + repeat("─", INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + CLOCK + center(title, INNER_W) + RESET + BORDER + "│" + RESET);
        System.out.println(BORDER + "└" + repeat("─", INNER_W) + "┘" + RESET);
    }

    // ── Sim label ─────────────────────────────────────────────────────────────
    private static String simLabel(String name, String age, String gender) {
        return BRIGHT_WHITE + name + RESET
                + MUTED + " (" + age + gender.charAt(0) + ")" + RESET;
    }

    private static void showCommitted(List<String[]> committed) {
        if (committed.isEmpty()) {
            return;
        }
        System.out.println("  " + MUTED + "Sims added so far:" + RESET);
        for (String[] d : committed) {
            System.out.println("    " + BRIGHT_BLACK + "•" + RESET + " " + simLabel(d[0], d[1], d[2]));
        }
        System.out.println();
    }

    // ── ANSI string utilities ─────────────────────────────────────────────────
    private static String stripAnsi(String s) {
        if (s == null) {
            return "";
        }
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    private static int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    private static String padColoured(String s, int width) {
        if (s == null) {
            s = "";
        }
        String plain = stripAnsi(s);
        if (plain.length() > width) {
            return String.format("%-" + width + "s", plain.substring(0, width));
        }
        return s + repeat(" ", width - plain.length());
    }

    private static String centerColoured(String s, int width) {
        int vlen = visibleLength(s);
        if (vlen >= width) {
            return padColoured(s, width);
        }
        int total = width - vlen;
        int lpad = total / 2;
        return repeat(" ", lpad) + s + repeat(" ", total - lpad);
    }

    private static String pad(String s, int width) {
        if (s == null) {
            s = "";
        }
        if (s.length() > width) {
            s = s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }

    private static String center(String s, int width) {
        if (s.length() >= width) {
            return pad(s, width);
        }
        int total = width - s.length();
        int lpad = total / 2;
        return repeat(" ", lpad) + s + repeat(" ", total - lpad);
    }

    private static String repeat(String ch, int n) {
        if (n <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(n * ch.length());
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    private static List<String> wordWrap(String s, int width) {
        List<String> result = new ArrayList<>();
        while (s.length() > width) {
            int cut = s.lastIndexOf(' ', width);
            if (cut <= 0) {
                cut = width;
            }
            result.add(s.substring(0, cut));
            s = s.substring(cut).stripLeading();
        }
        if (!s.isEmpty()) {
            result.add(s);
        }
        return result;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
