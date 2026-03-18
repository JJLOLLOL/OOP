package ui;

import Types.InteractionType;
import core.GameState;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.Location;
import models.SimCharacter;
import models.furnitureactions.Furniture;
import models.needs.Need;
import services.*;

/**
 * Single source of truth for all CLI output.
 *
 * Colour scheme: Header BOLD + CYAN Name BOLD + WHITE Age/gender WHITE Need
 * labels GRAY Need bars/nums GREEN / YELLOW / RED by value Money YELLOW
 * Location label GRAY, name CYAN NPC name WHITE, status GRAY, score GREEN if
 * positive Action title BOLD + CYAN Action numbers YELLOW Action text WHITE
 * Notif title CYAN Notif normal WHITE Notif positive GREEN Notif warning YELLOW
 * Notif critical RED Borders GRAY
 *
 * ANSI note: all padding uses padColoured() / visibleLength() which strip
 * escape codes before measuring, so borders are never pushed out of alignment.
 *
 * Bar chars: plain ASCII # and - (east_asian_width=Narrow, always 1 terminal
 * column).
 */
public class Renderer {

    // ── ANSI colours ──────────────────────────────────────────────────────────
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String WHITE = "\u001B[37m";
    private static final String GRAY = "\u001B[90m";

    // ── Layout ────────────────────────────────────────────────────────────────
    private static final int LEFT_W = 26;
    private static final int MID_W = 26;
    private static final int RIGHT_W = 22;
    // INNER_W = (LEFT_W+2)+1+(MID_W+2)+1+(RIGHT_W+2) = 28+1+28+1+24 = 82
    private static final int INNER_W = (LEFT_W + 2) + 1 + (MID_W + 2) + 1 + (RIGHT_W + 2);
    private static final int BAR_WIDTH = 10;

    // ══════════════════════════════════════════════════════════════════════════
    //  Top-level dispatch
    // ══════════════════════════════════════════════════════════════════════════
    public static void render(GameState state, WorldRegistry world) {
        clearScreen();
        switch (state.getPhase()) {
            case CREATE_SIM ->
                renderCreateSim(state);
            case PLAYING ->
                renderPlaying(state, world);
            case QUIT -> {
            }
        }
    }

    public static void showError(String message) {
        System.out.println("  " + RED + "[!] " + message + RESET);
        System.out.print("> ");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CREATE SIM phase
    // ══════════════════════════════════════════════════════════════════════════
    private static void renderCreateSim(GameState state) {
        printBanner("CREATE YOUR SIMS");
        System.out.println();

        CreateSimService.Step step = CreateSimService.getStep();

        switch (step) {
            case COUNT ->
                System.out.println("  How many Sims do you want to create?");
            case NAME -> {
                showCommitted(CreateSimService.getCommitted());
                System.out.printf("  Creating Sim %d of %d%n",
                        CreateSimService.getCurrentIndex() + 1,
                        CreateSimService.getTotalSims());
                System.out.println("  Enter name:");
            }
            case AGE -> {
                showCommitted(CreateSimService.getCommitted());
                System.out.println("  Name : " + CreateSimService.getInFlightName());
                System.out.println("  Enter age:");
            }
            case GENDER -> {
                showCommitted(CreateSimService.getCommitted());
                System.out.println("  Name : " + CreateSimService.getInFlightName());
                System.out.println("  Age  : " + CreateSimService.getInFlightAge());
                System.out.println("  Enter gender:");
            }
            case CONFIRM -> {
                System.out.println("  Review your Sims:\n");
                List<String[]> committed = CreateSimService.getCommitted();
                for (int i = 0; i < committed.size(); i++) {
                    String[] d = committed.get(i);
                    System.out.printf("    %d. %-15s Age %-4s  %s%n", i + 1, d[0], d[1], d[2]);
                }
                System.out.println("\n  Confirm? (Y / N)");
            }
            case PICK_PLAYER -> {
                System.out.println("  Choose your active Sim:\n");
                List<SimCharacter> sims = state.getSims();
                for (int i = 0; i < sims.size(); i++) {
                    SimCharacter s = sims.get(i);
                    System.out.printf("    %d. %s  (Age %d, %s)%n",
                            i + 1, s.getName(), s.getAge(), s.getGender());
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
        PlayService.Step step = PlayService.getStep();

        List<String> left = buildLeftPanel(player, loc, state, world);
        List<String> mid = buildMiddlePanel(step, loc, player, state, world);
        List<String> right = buildRightPanel(player);

        // Header: BOLD + CYAN
        String clock = BOLD + CYAN
                + "DAY " + state.getGameClock().getDays()
                + " - " + String.format("%02d:%02d",
                        state.getGameClock().getHours(),
                        state.getGameClock().getMinutes())
                + RESET;

        // Box top
        System.out.println(GRAY + "┌" + repeat("─", INNER_W) + "┐" + RESET);

        // Clock header row
        System.out.println(GRAY + "│" + RESET + centerColoured(clock, INNER_W) + GRAY + "│" + RESET);

        // Column separator
        System.out.println(GRAY + "├" + repeat("─", LEFT_W + 2)
                + "┬" + repeat("─", MID_W + 2)
                + "┬" + repeat("─", RIGHT_W + 2) + "┤" + RESET);

        // Body rows
        int rows = Math.max(left.size(), Math.max(mid.size(), right.size()));
        for (int i = 0; i < rows; i++) {
            String l = i < left.size() ? left.get(i) : "";
            String m = i < mid.size() ? mid.get(i) : "";
            String r = i < right.size() ? right.get(i) : "";
            printRow(l, m, r);
        }

        // Bottom border
        System.out.println(GRAY + "└" + repeat("─", LEFT_W + 2)
                + "┴" + repeat("─", MID_W + 2)
                + "┴" + repeat("─", RIGHT_W + 2) + "┘" + RESET);

        System.out.print("\n> ");
    }

    // ── Panel builders ────────────────────────────────────────────────────────
    private static List<String> buildLeftPanel(SimCharacter player, Location loc,
            GameState state, WorldRegistry world) {
        List<String> lines = new ArrayList<>();

        // Name (BOLD WHITE) + age/gender (WHITE)
        lines.add(BOLD + WHITE + player.getName() + RESET
                + WHITE + " (" + player.getAge() + player.getGender().charAt(0) + ")" + RESET);

        // Need bars
        for (Map.Entry<String, Need> e : player.getNeeds().entrySet()) {
            lines.add(needBar(e.getValue()));
        }

        // Money: YELLOW
        lines.add(YELLOW + String.format("Money: $%.2f", player.getMoney()) + RESET);

        // Divider: GRAY
        lines.add(GRAY + repeat("─", LEFT_W) + RESET);

        // Location label GRAY, name CYAN
        lines.add(GRAY + "Location: " + RESET + CYAN + loc.getLocationName() + RESET);

        // Nearby characters
        List<models.Character> chars = PlayService.charsAt(loc, state, world);
        if (chars.isEmpty()) {
            lines.add(GRAY + "No one nearby." + RESET);
        } else {
            lines.add(GRAY + "People Here:" + RESET);
            for (models.Character c : chars) {
                String status = state.getRelationshipManager().getStatus(player, c);
                int score = state.getRelationshipManager().getScore(player, c);
                lines.add(WHITE + c.getName() + RESET);
                String scoreColour = score > 0 ? GREEN : score < 0 ? RED : YELLOW;
                lines.add(GRAY + "- " + status + " " + RESET + scoreColour + "(" + score + ")" + RESET);
            }
        }

        return lines;
    }

    private static List<String> buildMiddlePanel(PlayService.Step step, Location loc,
            SimCharacter player, GameState state,
            WorldRegistry world) {
        List<String> lines = new ArrayList<>();

        switch (step) {
            case MAIN -> {
                // Title: BOLD + CYAN
                lines.add(BOLD + CYAN + "Actions" + RESET);
                lines.add(menuItem("1", "Interactables"));
                lines.add(menuItem("2", "Socialise"));
                lines.add(menuItem("3", "Change Location"));
                lines.add(menuItem("4", "Switch Character"));
                lines.add(menuItem("5", "Exit Game"));
            }
            case INTERACTABLES -> {
                lines.add(BOLD + CYAN + "Interactables" + RESET);
                List<Furniture> flist = loc.getFurnitures();
                for (int i = 0; i < flist.size(); i++) {
                    lines.add(menuItem(String.valueOf(i + 1), flist.get(i).getName()));
                }
                lines.add(backItem());
            }
            case INTERACTABLE_ACTION -> {
                Furniture f = PlayService.getSelectedFurniture();
                lines.add(BOLD + CYAN + f.getName() + RESET);
                List<String> actions = new ArrayList<>(f.getActionNames());
                for (int i = 0; i < actions.size(); i++) {
                    lines.add(menuItem(String.valueOf(i + 1), actions.get(i)));
                }
                lines.add(backItem());
            }
            case SOCIALISE -> {
                lines.add(BOLD + CYAN + "Socialise" + RESET);
                List<models.Character> chars = PlayService.charsAt(loc, state, world);
                if (chars.isEmpty()) {
                    lines.add(GRAY + "Nobody here." + RESET);
                } else {
                    for (int i = 0; i < chars.size(); i++) {
                        models.Character c = chars.get(i);
                        String status = state.getRelationshipManager().getStatus(player, c);
                        lines.add(menuItem(String.valueOf(i + 1),
                                c.getName() + " " + GRAY + "[" + status + "]" + RESET));
                    }
                }
                lines.add(backItem());
            }
            case SOCIALISE_ACTION -> {
                models.Character target = PlayService.getSelectedCharacter();
                lines.add(BOLD + CYAN + "Interact: " + WHITE + target.getName() + RESET);
                InteractionType[] types = InteractionType.values();
                for (int i = 0; i < types.length; i++) {
                    lines.add(menuItem(String.valueOf(i + 1), types[i].getLabel()));
                }
                lines.add(backItem());
            }
            case CHANGE_LOCATION -> {
                lines.add(BOLD + CYAN + "Go to..." + RESET);
                List<Location> locs = new ArrayList<>(world.getAllLocations());
                for (int i = 0; i < locs.size(); i++) {
                    boolean here = locs.get(i).equals(loc);
                    String label = locs.get(i).getLocationName()
                            + (here ? " " + YELLOW + "(here)" + RESET : "");
                    lines.add(menuItem(String.valueOf(i + 1), label));
                }
                lines.add(backItem());
            }
            case SWITCH_CHARACTER -> {
                lines.add(BOLD + CYAN + "Switch Sim" + RESET);
                List<SimCharacter> sims = state.getSims();
                for (int i = 0; i < sims.size(); i++) {
                    boolean active = sims.get(i).equals(player);
                    String label = sims.get(i).getName()
                            + (active ? " " + YELLOW + "(active)" + RESET : "");
                    lines.add(menuItem(String.valueOf(i + 1), label));
                }
                lines.add(backItem());
            }
        }

        return lines;
    }

    private static List<String> buildRightPanel(SimCharacter player) {
        List<String> lines = new ArrayList<>();

        // Title: CYAN
        lines.add(BOLD + CYAN + "Notifications" + RESET);

        List<String> notes = player.getNotifications();
        if (notes.isEmpty()) {
            lines.add(GRAY + "None." + RESET);
        } else {
            for (String note : notes) {
                // Classify the notification to pick the right colour
                String colour = classifyNotification(note);
                // Split on embedded newlines from RelationshipManager, then word-wrap
                for (String segment : note.split("\n")) {
                    String clean = segment.trim();
                    if (clean.isEmpty()) {
                        continue;
                    }
                    for (String line : wordWrap(clean, RIGHT_W)) {
                        lines.add(colour + line + RESET);
                    }
                }
                lines.add(""); // blank separator between notifications
            }
            // Remove trailing blank
            if (!lines.isEmpty() && stripAnsi(lines.get(lines.size() - 1)).isBlank()) {
                lines.remove(lines.size() - 1);
            }
        }

        return lines;
    }

    // ── Notification classifier ───────────────────────────────────────────────
    /**
     * Returns the ANSI colour to use for a notification based on its content.
     * GREEN — positive outcome (improved, unlocked, gained) RED — critical /
     * failure / worsened / critically low YELLOW — warning / unchanged / cost
     * WHITE — neutral information
     */
    private static String classifyNotification(String note) {
        String lower = note.toLowerCase();
        if (lower.contains("improved") || lower.contains("unlocked")
                || lower.contains("gained") || lower.contains("level up")
                || lower.contains("promoted")) {
            return GREEN;
        }
        if (lower.contains("failed") || lower.contains("worsened")
                || lower.contains("critically") || lower.contains("can't")
                || lower.contains("cannot") || lower.contains("not enough")) {
            return RED;
        }
        if (lower.contains("warning") || lower.contains("low")
                || lower.contains("unchanged") || lower.contains("cost")) {
            return YELLOW;
        }
        return WHITE;
    }

    // ── Box-drawing ───────────────────────────────────────────────────────────
    private static void printRow(String l, String m, String r) {
        System.out.println(GRAY + "│" + RESET + " " + padColoured(l, LEFT_W)
                + " " + GRAY + "│" + RESET + " " + padColoured(m, MID_W)
                + " " + GRAY + "│" + RESET + " " + padColoured(r, RIGHT_W)
                + " " + GRAY + "│" + RESET);
    }

    // ── Menu item helpers ─────────────────────────────────────────────────────
    /**
     * "1. Action text" — number YELLOW, text WHITE
     */
    private static String menuItem(String num, String label) {
        return YELLOW + num + ". " + RESET + WHITE + label + RESET;
    }

    /**
     * "0. Back" — muted
     */
    private static String backItem() {
        return GRAY + "0. Back" + RESET;
    }

    // ── ANSI-aware string helpers ─────────────────────────────────────────────
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
            // Strip colour and hard-truncate to preserve alignment
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    // ── Need bar ──────────────────────────────────────────────────────────────
    /**
     * "Social [##########] 84" label 8 bar 12 val 3 = 25 visible chars (fits
     * LEFT_W=26)
     *
     * Label → GRAY Bar fill → GREEN / YELLOW / RED Bar empty→ GRAY Number →
     * matches bar colour
     */
    private static String needBar(Need need) {
        int val = (int) need.getValue();
        int filled = val * BAR_WIDTH / 100;
        int empty = BAR_WIDTH - filled;

        String barColour;
        if (val >= 70) {
            barColour = GREEN; 
        }else if (val >= 40) {
            barColour = YELLOW; 
        }else {
            barColour = RED;
        }

        String label = GRAY + String.format("%-8s", need.getNeedName()) + RESET;
        String bar = GRAY + "[" + RESET
                + barColour + repeat("#", filled) + RESET
                + GRAY + repeat("-", empty) + RESET
                + GRAY + "]" + RESET;
        String valueStr = barColour + String.format("%3d", val) + RESET;

        return label + " " + bar + " " + valueStr;
    }

    // ── Word wrap (plain strings only) ────────────────────────────────────────
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

    // ── Misc ──────────────────────────────────────────────────────────────────
    private static void printBanner(String title) {
        System.out.println(GRAY + "┌" + repeat("─", INNER_W) + "┐" + RESET);
        System.out.println(GRAY + "│" + RESET + center(title, INNER_W) + GRAY + "│" + RESET);
        System.out.println(GRAY + "└" + repeat("─", INNER_W) + "┘" + RESET);
    }

    private static void showCommitted(List<String[]> committed) {
        if (!committed.isEmpty()) {
            System.out.println("  Sims added so far:");
            for (String[] d : committed) {
                System.out.printf("    • %-15s Age %-4s  %s%n", d[0], d[1], d[2]);
            }
            System.out.println();
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
