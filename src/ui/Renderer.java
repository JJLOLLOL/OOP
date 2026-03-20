package ui;

import Types.*;
import core.CreateSimController;
import core.GameState;
import core.PlayController;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.Location;
import models.SimCharacter;
import models.Skills;
import models.actions.Furniture;
import models.actions.FurnitureAction;
import models.needs.Need;
import services.NotificationService;

public class Renderer {

    // ── ANSI ──────────────────────────────────────────────────────────────────
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String WHITE = "\u001B[37m";
    private static final String BRIGHT_BLACK = "\u001B[90m";
    private static final String BRIGHT_RED = "\u001B[91m";
    private static final String BRIGHT_GREEN = "\u001B[92m";
    private static final String BRIGHT_YELLOW = "\u001B[93m";
    private static final String BRIGHT_BLUE = "\u001B[94m";
    private static final String BRIGHT_MAGENTA = "\u001B[95m";
    private static final String BRIGHT_CYAN = "\u001B[96m";
    private static final String BRIGHT_WHITE = "\u001B[97m";

    // ── Semantic aliases ──────────────────────────────────────────────────────
    private static final String BORDER = BRIGHT_BLACK;
    private static final String LABEL = BRIGHT_BLACK;
    private static final String MUTED = BRIGHT_BLACK;
    private static final String TITLE = BOLD + BRIGHT_CYAN;
    private static final String CLOCK = BOLD + BRIGHT_WHITE;
    private static final String SIM_NAME = BOLD + BRIGHT_WHITE;

    // ── Layout ────────────────────────────────────────────────────────────────
    private static final int MIN_COL_W = 28;
    private static final int BAR_WIDTH = 10;
    private static int LEFT_W = MIN_COL_W;
    private static int MID_W = MIN_COL_W;
    private static int SKILLS_W = MIN_COL_W;
    private static int NOTIF_W = MIN_COL_W;
    private static int INNER_W = 4 * (MIN_COL_W + 2) + 3;

    // ── Public API ────────────────────────────────────────────────────────────
    public static void render(GameState state, WorldRegistry world) {
        clearScreen();
        switch (state.getPhase()) {
            case CREATE_SIM -> renderCreateSim(state);
            case PLAYING -> renderPlaying(state, world);
            case QUIT -> {}
        }
    }

    public static void showError(String message) {
        System.out.println("  " + BRIGHT_RED + "[!] " + RESET + WHITE + message + RESET);
        System.out.print("> ");
    }

    // ── CREATE SIM ────────────────────────────────────────────────────────────
    private static void renderCreateSim(GameState state) {
        printBanner("CREATE YOUR SIMS");
        System.out.println();
        switch (CreateSimController.getStep()) {
            case COUNT -> prompt("How many Sims do you want to create?");
            case NAME -> {
                showCommitted(CreateSimController.getCommitted());
                System.out.printf("  " + MUTED + "Creating Sim %d of %d%n" + RESET,
                        CreateSimController.getCurrentIndex() + 1, CreateSimController.getTotalSims());
                prompt("Enter name:");
            }
            case AGE -> {
                showCommitted(CreateSimController.getCommitted());
                field("Name", CreateSimController.getInFlightName());
                prompt("Enter age:");
            }
            case GENDER -> {
                showCommitted(CreateSimController.getCommitted());
                field("Name", CreateSimController.getInFlightName());
                field("Age", CreateSimController.getInFlightAge());
                prompt("Enter gender (M / F):");
            }
            case CONFIRM -> {
                System.out.println("  " + TITLE + "Review your Sims:" + RESET + "\n");
                List<String[]> committed = CreateSimController.getCommitted();
                for (int i = 0; i < committed.size(); i++) {
                    String[] data = committed.get(i);
                    System.out.printf("    " + BRIGHT_YELLOW + "%d. " + RESET + BRIGHT_WHITE + "%s" + RESET + "%n", i + 1, simLabel(data[0], data[1], data[2]));
                }
                System.out.println("\n  " + LABEL + "Confirm? " + RESET + BRIGHT_GREEN + "(Y)" + RESET + " / " + BRIGHT_RED + "(N)" + RESET);
            }
            case PICK_PLAYER -> {
                System.out.println("  " + TITLE + "Choose your active Sim:" + RESET + "\n");
                for (int i = 0; i < state.getSims().size(); i++) {
                    SimCharacter s = state.getSims().get(i);
                    System.out.printf("    " + BRIGHT_YELLOW + "%d. " + RESET + BRIGHT_WHITE + "%s" + RESET + "%n", i + 1, simLabel(s.getName(), String.valueOf(s.getAge()), s.getGender()));
                }
            }
        }
        System.out.print("\n> ");
    }

    // ── PLAYING ───────────────────────────────────────────────────────────────
    private static void renderPlaying(GameState state, WorldRegistry world) {
        SimCharacter player = state.getActivePlayer();
        Location loc = player.getLocation();
        PlayController.Step step = PlayController.getStep();

        List<String> stats = buildStatsPanel(player, loc, state, world);
        List<String> actions = buildActionsPanel(step, loc, player, state, world);
        List<String> skills = buildSkillsPanel(player);
        List<String> notifs = buildNotificationsPanel(player);

        LEFT_W = Math.max(MIN_COL_W, maxVisible(stats));
        MID_W = Math.max(MIN_COL_W, maxVisible(actions));
        SKILLS_W = Math.max(MIN_COL_W, maxVisible(skills));
        NOTIF_W = Math.max(MIN_COL_W, maxVisible(notifs));
        INNER_W = (LEFT_W + 2) + (MID_W + 2) + (SKILLS_W + 2) + (NOTIF_W + 2) + 3;

        printBoxTop(CLOCK + "DAY " + state.getGameClock().getDays() + "  ─  " +
                    String.format("%02d:%02d", state.getGameClock().getHours(), state.getGameClock().getMinutes()) + RESET);

        System.out.println(BORDER + "├" + seg(LEFT_W + 2) + "┬" + seg(MID_W + 2) + "┬" + seg(SKILLS_W + 2) +
                    "┬" + seg(NOTIF_W + 2) + "┤" + RESET);

        int rows = Math.max(Math.max(stats.size(), actions.size()), Math.max(skills.size(), notifs.size()));
        for (int i = 0; i < rows; i++) {
            System.out.println(
                    BORDER + "│" + RESET + " " + padColoured(get(stats, i), LEFT_W)
                    + " " + BORDER + "│" + RESET + " " + padColoured(get(actions, i), MID_W)
                    + " " + BORDER + "│" + RESET + " " + padColoured(get(skills, i), SKILLS_W)
                    + " " + BORDER + "│" + RESET + " " + padColoured(get(notifs, i), NOTIF_W)
                    + " " + BORDER + "│" + RESET);
        }

        System.out.println(BORDER + "└" + seg(LEFT_W + 2) + "┴" + seg(MID_W + 2) + "┴" + seg(SKILLS_W + 2) +
                    "┴" + seg(NOTIF_W + 2) + "┘" + RESET);
        System.out.print("\n> ");
    }

    // ── Stats panel ───────────────────────────────────────────────────────────
    private static List<String> buildStatsPanel(SimCharacter player, Location loc,
            GameState state, WorldRegistry world) {
        List<String> lines = new ArrayList<>();

        lines.add(SIM_NAME + player.getName() + RESET
                + MUTED + " (" + player.getAge() + player.getGender().charAt(0) + ")" + RESET);

        models.Career career = player.getCareer();
        lines.add(career.getCurrentCareer() != CareerList.JOBLESS
                ? BRIGHT_MAGENTA + career.getTitle() + RESET + MUTED + "  " + career.getRank() + RESET
                : MUTED + "Unemployed" + RESET);
        lines.add("");

        for (Need need : player.getNeeds().values()) {
            lines.add(bar(
                    need.getNeedName(), 8, (int) need.getValue(), 100,
                    need.getValue() >= 70 ? BRIGHT_GREEN : need.getValue() >= 40 ? BRIGHT_YELLOW : BRIGHT_RED,
                    String.format("%3d%%", (int) need.getValue())));
        }

        lines.add("");
        lines.add(BRIGHT_YELLOW + "Money: $" + String.format("%.2f", player.getMoney()) + RESET);
        lines.add(BORDER + "─".repeat(LEFT_W) + RESET);
        lines.add(LABEL + "At " + RESET + BRIGHT_CYAN + loc.getLocationName() + RESET);

        List<models.Character> chars = PlayController.charsAt(loc, state, world);
        if (chars.isEmpty()) {
            lines.add(MUTED + "No one nearby." + RESET);
        } else {
            lines.add(LABEL + "nearby:" + RESET);
            for (models.Character c : chars) {
                RelationshipList status = state.getRelationshipService().getStatus(player, c);
                int score = state.getRelationshipService().getScore(player, c);
                String col = score > 0 ? BRIGHT_GREEN : score < 0 ? BRIGHT_RED : BRIGHT_YELLOW;
                lines.add(WHITE + c.getName() + RESET + MUTED + " [" + status.label + "] " + RESET + col + score + RESET);
                if (c instanceof models.NPCCharacter npc && npc.getDescription() != null && !npc.getDescription().isBlank()) {
                    lines.add(MUTED + "  " + npc.getDescription() + RESET);
                }
            }
        }
        return lines;
    }

    // ── Actions panel ─────────────────────────────────────────────────────────
    private static List<String> buildActionsPanel(PlayController.Step step, Location loc,
            SimCharacter player, GameState state, WorldRegistry world) {
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
                List<FurnitureAction> acts = new ArrayList<>(f.getActions());
                acts.sort((a, b) -> a.getName().compareTo(b.getName()));
                for (int i = 0; i < acts.size(); i++) {
                    FurnitureAction act = acts.get(i);
                    lines.add(menuItem(String.valueOf(i + 1), act.getName()));
                    addEffectLines(lines, "  needs", act.affectedNeedsByActionMap(), true);
                    addEffectLines(lines, " skills", act.affectedSkillsByActionMap(), false);
                    if (act.moneyDeducted() > 0) {
                        lines.add(MUTED + "   cost: " + RESET + BRIGHT_YELLOW + "$" + String.format("%.0f", act.moneyDeducted()) + RESET);
                    }
                    if (act.getTimeRequired() > 0) {
                        lines.add(MUTED + "   time: " + RESET + BRIGHT_WHITE + formatHours(act.getTimeRequired()) + RESET);
                    }
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
                        RelationshipList status = state.getRelationshipService().getStatus(player, chars.get(i));
                        lines.add(menuItem(String.valueOf(i + 1),
                                chars.get(i).getName() + " " + MUTED + "[" + status.label + "]" + RESET));
                    }
                }
                lines.add(backItem());
            }
            case SOCIALISE_ACTION -> {
                lines.add(menuTitle("Interact: " + PlayController.getSelectedCharacter().getName()));
                InteractionList[] types = InteractionList.values();
                for (int i = 0; i < types.length; i++) {
                    lines.add(menuItem(String.valueOf(i + 1), types[i].getLabel()));
                }
                lines.add(backItem());
            }
            case CHANGE_LOCATION -> {
                lines.add(menuTitle("Go to..."));
                List<Location> locs = new ArrayList<>(world.getAllLocations());
                for (int i = 0; i < locs.size(); i++) {
                    String label = locs.get(i).getLocationName()
                            + (locs.get(i).equals(loc) ? " " + BRIGHT_CYAN + "← here" + RESET : "");
                    lines.add(menuItem(String.valueOf(i + 1), label));
                }
                lines.add(backItem());
            }
            case SWITCH_CHARACTER -> {
                lines.add(menuTitle("Switch Sim"));
                List<SimCharacter> sims = state.getSims();
                for (int i = 0; i < sims.size(); i++) {
                    String label = sims.get(i).getName()
                            + (sims.get(i).equals(player) ? " " + BRIGHT_GREEN + "← active" + RESET : "");
                    lines.add(menuItem(String.valueOf(i + 1), label));
                }
                lines.add(backItem());
            }
            case PICK_CAREER -> {
                lines.add(menuTitle("Choose Career"));
                lines.add("");
                List<CareerList> careers = PlayController.getAvailableCareers();
                int tw = careers.stream().mapToInt(c -> c.getTitle().length()).max().orElse(10) + 2;
                lines.add(MUTED + "    " + pad("Career", tw) + "  " + pad("Salary", 9)
                        + "  " + pad("Hours", 5) + "  Skills" + RESET);
                lines.add(MUTED + "    " + "─".repeat(tw + 36) + RESET);
                for (int i = 0; i < careers.size(); i++) {
                    CareerList c = careers.get(i);
                    lines.add(BRIGHT_YELLOW + (i + 1) + ". " + RESET
                            + BRIGHT_WHITE + pad(c.getTitle(), tw) + RESET
                            + "  " + MUTED + pad(String.format("$%.0f/d", c.getBaseSalary()), 9) + RESET
                            + "  " + MUTED + pad(c.getWorkingHours() > 0 ? (int) c.getWorkingHours() + "h" : "", 5) + RESET
                            + "  " + BRIGHT_BLACK + String.join(", ", c.getRelatedSkills()) + RESET);
                }
                lines.add("");
                lines.add(backItem());
            }
        }
        return lines;
    }

    // ── Skills panel ──────────────────────────────────────────────────────────
    private static List<String> buildSkillsPanel(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(menuTitle("Skills"));
        for (Skills skill : player.getAllSkills().values()) {
            int pct = (int) Math.min(100, (skill.getProgress() / skill.getRequiredXP()) * 100);
            lines.add(bar(skill.getSkillName(), 11, pct, 100,
                    pct >= 70 ? BRIGHT_GREEN : pct >= 40 ? BRIGHT_YELLOW : BRIGHT_BLUE,
                    MUTED + "Lv" + skill.getLevel() + RESET));
        }
        return lines;
    }

    // ── Notifications panel ───────────────────────────────────────────────────
    private static List<String> buildNotificationsPanel(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(menuTitle("Notifications"));
        List<String> notes = NotificationService.get(player);
        if (notes.isEmpty()) {
            lines.add(MUTED + "None." + RESET);
            return lines;
        }
        for (String note : notes) {
            String colour = classifyNotification(note);
            for (String seg : note.split("\n")) {
                String clean = seg.trim();
                if (!clean.isEmpty()) {
                    wordWrap(clean, NOTIF_W).forEach(l -> lines.add(colour + l + RESET));
                }
            }
            lines.add("");
        }
        while (!lines.isEmpty() && stripAnsi(lines.get(lines.size() - 1)).isBlank()) {
            lines.remove(lines.size() - 1);
        }
        return lines;
    }

    // ── Shared helpers ────────────────────────────────────────────────────────
    private static void addEffectLines(List<String> lines, String labelKey,
            Map<String, Double> effects, boolean isNeeds) {
        if (effects == null || effects.isEmpty()) {
            return;
        }
        boolean first = true;
        for (Map.Entry<String, Double> e : new TreeMap<>(effects).entrySet()) {
            double v = e.getValue();
            String prefix = first ? MUTED + labelKey + ": " + RESET : "         ";
            String value = isNeeds
                    ? (v > 0 ? BRIGHT_GREEN : BRIGHT_RED) + (v > 0 ? "+" : "") + (int) v + " " + e.getKey() + RESET
                    : BRIGHT_CYAN + "+" + (int) v + "xp " + e.getKey() + RESET;
            lines.add(prefix + value);
            first = false;
        }
    }

    private static String bar(String name, int nameWidth, int value, int max, String colour, String suffix) {
        int filled = value * BAR_WIDTH / max;
        int empty = BAR_WIDTH - filled;
        return LABEL + String.format("%-" + nameWidth + "s", name) + RESET
                + " " + MUTED + "[" + RESET + colour + "#".repeat(filled) + RESET
                + MUTED + "-".repeat(empty) + "]" + RESET
                + " " + colour + suffix + RESET;
    }

    private static String classifyNotification(String note) {
        String l = note.toLowerCase();
        if (l.contains("levelled up") || l.contains("improved") || l.contains("promoted")
                || l.contains("earned") || l.contains("started career")) {
            return BRIGHT_GREEN;
        }
        if (l.contains("failed") || l.contains("starving") || l.contains("exhausted")
                || l.contains("cannot") || l.contains("not enough")) {
            return BRIGHT_RED;
        }
        if (l.contains("warning") || l.contains("low") || l.contains("lonely")
                || l.contains("bored") || l.contains("dirty") || l.contains("cost")) {
            return BRIGHT_YELLOW;
        }
        return BRIGHT_WHITE;
    }

    private static String menuTitle(String t) {
        return TITLE + t + RESET;
    }

    private static String menuItem(String n, String l) {
        return BRIGHT_YELLOW + n + "." + RESET + " " + WHITE + l + RESET;
    }

    private static String backItem() {
        return MUTED + "0. Back" + RESET;
    }

    private static String get(List<String> list, int i) {
        return i < list.size() ? list.get(i) : "";
    }

    private static String seg(int n) {
        return "─".repeat(n);
    }

    private static void prompt(String text) {
        System.out.println("  " + LABEL + text + RESET);
    }

    private static void field(String key, String val) {
        System.out.println("  " + MUTED + pad(key, 4) + " : " + RESET + BRIGHT_WHITE + val + RESET);
    }

    private static void printBoxTop(String clock) {
        System.out.println(BORDER + "┌" + seg(INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + centerColoured(clock, INNER_W) + BORDER + "│" + RESET);
    }

    private static void printBanner(String title) {
        System.out.println(BORDER + "┌" + seg(INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + CLOCK + center(title, INNER_W) + RESET + BORDER + "│" + RESET);
        System.out.println(BORDER + "└" + seg(INNER_W) + "┘" + RESET);
    }

    private static String simLabel(String name, String age, String gender) {
        return BRIGHT_WHITE + name + RESET + MUTED + " (" + age + gender.charAt(0) + ")" + RESET;
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

    // ── ANSI / string utilities ───────────────────────────────────────────────
    private static String stripAnsi(String s) {
        return s == null ? "" : s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    private static int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    private static int maxVisible(List<String> lines) {
        return lines.stream().mapToInt(Renderer::visibleLength).max().orElse(0);
    }

    private static String padColoured(String s, int width) {
        if (s == null) {
            s = "";
        }
        String plain = stripAnsi(s);
        if (plain.length() > width) {
            return String.format("%-" + width + "s", plain.substring(0, width));
        }
        return s + " ".repeat(width - plain.length());
    }

    private static String centerColoured(String s, int width) {
        int vlen = visibleLength(s);
        if (vlen >= width) {
            return padColoured(s, width);
        }
        int lpad = (width - vlen) / 2;
        return " ".repeat(lpad) + s + " ".repeat(width - vlen - lpad);
    }

    private static String pad(String s, int width) {
        if (s == null) {
            s = "";
        }
        return String.format("%-" + width + "s", s.length() > width ? s.substring(0, width) : s);
    }

    private static String center(String s, int width) {
        if (s.length() >= width) {
            return pad(s, width);
        }
        int lpad = (width - s.length()) / 2;
        return " ".repeat(lpad) + s + " ".repeat(width - s.length() - lpad);
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

    private static String formatHours(double h) {
        if (h < 1.0) {
            return (int) (h * 60) + "min";
        }
        int hrs = (int) h, mins = (int) Math.round((h - hrs) * 60);
        return mins > 0 ? hrs + "h " + mins + "min" : hrs + "h";
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
