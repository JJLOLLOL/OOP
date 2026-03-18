package ui;

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
 * <h3>Layout (playing screen)</h3>
 * <pre>
 * ┌─────────────────────────── DAY 1 - 08:17 ───────────────────────────────────┐
 * ├────────────┬──────────────┬──────────────┬────────────────────────────────┤
 * │ Left       │ Middle       │ Skills       │ Notifications                   │
 * │ (stats)    │ (actions)    │ (skill bars) │ (gameplay + achievements)       │
 * └────────────┴──────────────┴──────────────┴────────────────────────────────┘
 * </pre>
 *
 * <h3>ANSI alignment rule</h3>
 * ANSI escape codes are invisible in the terminal but count toward
 * {@link String#length()}. All padding uses {@link #padColoured(String, int)}
 * and {@link #visibleLength(String)}, which strip escape codes before
 * measuring, so box borders are never pushed out of alignment.
 *
 * <h3>Bar character rule</h3> {@code █} and {@code ░} have
 * {@code east_asian_width=Ambiguous} and render as 2 columns in some terminals.
 * Plain ASCII {@code #} and {@code -} are always exactly 1 column wide and are
 * used instead.
 */
public class Renderer {

    // ── ANSI colour codes ─────────────────────────────────────────────────────
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String WHITE = "\u001B[37m";
    private static final String GRAY = "\u001B[90m";

    // ── Column widths (visible characters, excluding border chars) ────────────
    /**
     * All four columns share the same inner content width. Total box width = 4
     * × (COL_W + 2) + 3 dividers + 2 outer borders = 4 × 32 + 3 + 2 = 133
     * visible chars.
     */
    private static final int COL_W = 30;

    /**
     * Aliases so existing code stays readable.
     */
    private static final int LEFT_W = COL_W;
    private static final int MID_W = COL_W;
    private static final int SKILLS_W = COL_W;
    private static final int NOTIF_W = COL_W;

    /**
     * Total inner width of the box (between ┌ and ┐), used for the clock
     * header. Formula: 4 × (COL_W + 2) + 3 inner dividers = 4 × 32 + 3 = 131.
     */
    private static final int INNER_W = 4 * (COL_W + 2) + 3;

    /**
     * Number of {@code #}/{@code -} characters in a need progress bar.
     */
    private static final int BAR_WIDTH = 10;

    // ══════════════════════════════════════════════════════════════════════════
    //  Public API
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Clears the terminal and renders the appropriate screen for the current
     * game phase.
     *
     * @param state the live game state
     * @param world the world registry (locations and NPCs)
     */
    public static void render(GameState state, WorldRegistry world) {
        clearScreen();
        switch (state.getPhase()) {
            case CREATE_SIM ->
                renderCreateSim(state);
            case PLAYING ->
                renderPlaying(state, world);
            case QUIT -> {
                /* nothing to render */ }
        }
    }

    /**
     * Prints an inline error message directly below the current screen without
     * triggering a full redraw. Re-prints the input prompt afterwards.
     *
     * @param message the error text to display
     */
    public static void showError(String message) {
        System.out.println("  " + RED + "[!] " + message + RESET);
        System.out.print("> ");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CREATE SIM phase
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Renders the sim-creation wizard. Each step shows a prompt and, where
     * relevant, the sims that have already been committed in this session.
     */
    private static void renderCreateSim(GameState state) {
        printBanner("CREATE YOUR SIMS");
        System.out.println();

        switch (CreateSimController.getStep()) {

            case COUNT ->
                System.out.println("  How many Sims do you want to create?");

            case NAME -> {
                showCommitted(CreateSimController.getCommitted());
                System.out.printf("  Creating Sim %d of %d%n",
                        CreateSimController.getCurrentIndex() + 1,
                        CreateSimController.getTotalSims());
                System.out.println("  Enter name:");
            }

            case AGE -> {
                showCommitted(CreateSimController.getCommitted());
                System.out.println("  Name : " + CreateSimController.getInFlightName());
                System.out.println("  Enter age:");
            }

            case GENDER -> {
                showCommitted(CreateSimController.getCommitted());
                System.out.println("  Name : " + CreateSimController.getInFlightName());
                System.out.println("  Age  : " + CreateSimController.getInFlightAge());
                System.out.println("  Enter gender (M / F):");
            }

            case CONFIRM -> {
                System.out.println("  Review your Sims:\n");
                List<String[]> committed = CreateSimController.getCommitted();
                for (int i = 0; i < committed.size(); i++) {
                    String[] d = committed.get(i);
                    String label = simLabel(d[0], d[1], d[2]);
                    System.out.printf("    %d. %s%n", i + 1, label);
                }
                System.out.println("\n  Confirm? (Y / N)");
            }

            case PICK_PLAYER -> {
                System.out.println("  Choose your active Sim:\n");
                for (int i = 0; i < state.getSims().size(); i++) {
                    SimCharacter s = state.getSims().get(i);
                    System.out.printf("    %d. %s%n", i + 1,
                            simLabel(s.getName(), String.valueOf(s.getAge()), s.getGender()));
                }
            }
        }

        System.out.print("\n> ");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PLAYING phase
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Renders the main game screen: a full-width clock header followed by a
     * three-column panel layout (character info | actions | notifications).
     */
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
        printBodyRows(stats, skills, actions, notifs);
        printBoxBottom();

        System.out.print("\n> ");
    }

    // ── Box sections ──────────────────────────────────────────────────────────
    /**
     * Prints the top border with the clock title centered inside it.
     */
    private static void printBoxTop(String clock) {
        System.out.println(GRAY + "┌" + repeat("─", INNER_W) + "┐" + RESET);
        System.out.println(GRAY + "│" + RESET + centerColoured(clock, INNER_W) + GRAY + "│" + RESET);
    }

    /**
     * Prints the horizontal separator that splits the header from the four
     * columns.
     */
    private static void printColumnSeparator() {
        System.out.println(GRAY + "├" + repeat("─", LEFT_W + 2)
                + "┬" + repeat("─", MID_W + 2)
                + "┬" + repeat("─", SKILLS_W + 2)
                + "┬" + repeat("─", NOTIF_W + 2) + "┤" + RESET);
    }

    /**
     * Prints all body rows, padding shorter columns with empty lines so all
     * four columns reach the same height.
     */
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

    /**
     * Prints the bottom border of the four-column box.
     */
    private static void printBoxBottom() {
        System.out.println(GRAY + "└" + repeat("─", LEFT_W + 2)
                + "┴" + repeat("─", MID_W + 2)
                + "┴" + repeat("─", SKILLS_W + 2)
                + "┴" + repeat("─", NOTIF_W + 2) + "┘" + RESET);
    }

    // ── Panel builders ────────────────────────────────────────────────────────
    /**
     * Builds the left panel lines: character name, need bars, money, location,
     * and nearby characters with relationship status.
     */
    private static List<String> buildStatsPanel(SimCharacter player, Location loc,
            GameState state, WorldRegistry world) {
        List<String> lines = new ArrayList<>();

        // Character name (bold white) and age/gender (white)
        lines.add(BOLD + WHITE + player.getName() + RESET
                + WHITE + " (" + player.getAge() + player.getGender().charAt(0) + ")" + RESET);

        // One need bar per need
        for (Map.Entry<String, Need> e : player.getNeeds().entrySet()) {
            lines.add(needBar(e.getValue()));
        }

        // Money
        lines.add(YELLOW + String.format("Money: $%.2f", player.getMoney()) + RESET);

        // Visual divider before the location section
        lines.add(GRAY + repeat("─", LEFT_W) + RESET);

        // Current location
        lines.add(GRAY + "Location: " + RESET + CYAN + loc.getLocationName() + RESET);

        // Characters present at this location
        List<models.Character> chars = PlayController.charsAt(loc, state, world);
        if (chars.isEmpty()) {
            lines.add(GRAY + "No one nearby." + RESET);
        } else {
            lines.add(GRAY + "People here:" + RESET);
            for (models.Character c : chars) {
                String status = state.getRelationshipService().getStatus(player, c);
                int score = state.getRelationshipService().getScore(player, c);
                lines.add(WHITE + c.getName() + RESET);
                // Score colour: green if positive, red if negative, yellow if neutral
                String scoreColour = score > 0 ? GREEN : score < 0 ? RED : YELLOW;
                lines.add(GRAY + "- " + status + " " + RESET + scoreColour + "(" + score + ")" + RESET);
            }
        }

        return lines;
    }

    /**
     * Builds the middle panel lines based on the current
     * {@link PlayController.Step}. Each step renders a different numbered menu
     * or list.
     */
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
                List<String> actions = new ArrayList<>(f.getActionNames());
                for (int i = 0; i < actions.size(); i++) {
                    lines.add(menuItem(String.valueOf(i + 1), actions.get(i)));
                }
                lines.add(backItem());
            }

            case SOCIALISE -> {
                lines.add(menuTitle("Socialise"));
                List<models.Character> chars = PlayController.charsAt(loc, state, world);
                if (chars.isEmpty()) {
                    lines.add(GRAY + "Nobody here." + RESET);
                } else {
                    for (int i = 0; i < chars.size(); i++) {
                        models.Character c = chars.get(i);
                        String status = state.getRelationshipService().getStatus(player, c);
                        lines.add(menuItem(String.valueOf(i + 1),
                                c.getName() + " " + GRAY + "[" + status + "]" + RESET));
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
                            + (here ? " " + YELLOW + "(here)" + RESET : "");
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
                            + (active ? " " + YELLOW + "(active)" + RESET : "");
                    lines.add(menuItem(String.valueOf(i + 1), label));
                }
                lines.add(backItem());
            }
        }

        return lines;
    }

    /**
     * Builds the skills panel: one compact progress bar per skill.
     *
     * <p>
     * Format per line: {@code "Cooking  L3 [####------] 40"} where the bar width
     * is scaled to fit {@link #SKILLS_W}.
     */
    private static List<String> buildSkillsPanel(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(BOLD + CYAN + "Skills" + RESET);

        for (Map.Entry<String, models.Skills> e : player.getAllSkills().entrySet()) {
            lines.add(skillBar(e.getValue()));
        }

        return lines;
    }

    /**
     * Builds the notifications panel: all live notifications colour-coded by
     * severity and word-wrapped to fit {@link #NOTIF_W}.
     */
    private static List<String> buildNotificationsPanel(SimCharacter player) {
        List<String> lines = new ArrayList<>();
        lines.add(BOLD + CYAN + "Notifications" + RESET);

        List<String> notes = player.getNotifications();
        if (notes.isEmpty()) {
            lines.add(GRAY + "None." + RESET);
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

        // Remove trailing blank
        while (!lines.isEmpty() && stripAnsi(lines.get(lines.size() - 1)).isBlank()) {
            lines.remove(lines.size() - 1);
        }

        return lines;
    }

    // ── Notification classifier ───────────────────────────────────────────────
    /**
     * Returns the ANSI colour code appropriate for a notification string by
     * scanning it for known keywords.
     *
     * <ul>
     * <li>{@code GREEN} — positive outcome: improved, unlocked, gained,
     * promoted</li>
     * <li>{@code RED} — critical / failure: failed, worsened, critically, not
     * enough</li>
     * <li>{@code YELLOW} — warning: low, warning, unchanged, cost</li>
     * <li>{@code WHITE} — neutral / informational (default)</li>
     * </ul>
     *
     * @param note the raw notification string (may contain ANSI codes)
     * @return one of the ANSI colour constants defined in this class
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

    // ── Need bar ──────────────────────────────────────────────────────────────
    /**
     * Renders a single need as a coloured progress bar.
     *
     * <p>
     * Format: {@code "Social   [#######---] 70"}
     * <ul>
     * <li>Label — 8 visible chars, GRAY</li>
     * <li>Bar — 12 visible chars ({@code [} + 10 + {@code ]})</li>
     * <li>Value — 3 visible chars, colour-coded</li>
     * </ul>
     * Total visible width: 8+1+12+1+3 = 25, fits within {@code COL_W = 30}.
     *
     * <p>
     * Bar colour thresholds:
     * <ul>
     * <li>≥ 70 → GREEN</li>
     * <li>≥ 40 → YELLOW</li>
     * <li>&lt; 40 → RED</li>
     * </ul>
     *
     * @param need the need to render
     * @return a formatted, coloured string safe to pass to {@link #padColoured}
     */
    private static String needBar(Need need) {
        int val = (int) need.getValue();
        int filled = val * BAR_WIDTH / 100;
        int empty = BAR_WIDTH - filled;

        String barColour = val >= 70 ? GREEN : val >= 40 ? YELLOW : RED;

        // Longest need name is "Hygiene" (7 chars) — pad to 8 for alignment
        String label = GRAY + String.format("%-8s", need.getNeedName()) + RESET;
        String bar = GRAY + "[" + RESET
                + barColour + repeat("#", filled) + RESET
                + GRAY + repeat("-", empty) + RESET
                + GRAY + "]" + RESET;
        String valueStr = barColour + String.format("%3d", val) + RESET;

        return label + " " + bar + " " + valueStr;
    }

    /**
     * Renders a single skill as a progress bar styled in GRAY.
     *
     * <p>
     * Format: {@code "Programming L3 [##########]"}
     * <ul>
     * <li>Name — 11 visible chars (padded to longest name "Programming"),
     * GRAY</li>
     * <li>Level — 3 visible chars ({@code L} + level number), GRAY</li>
     * <li>Bar — 12 visible chars ({@code [} + 10 + {@code ]}), fill
     * colour-coded</li>
     * </ul>
     * Total: 11+1+3+1+12 = 28, fits within {@code COL_W = 30}. No percentage
     * shown — progress is communicated by level and bar fill.
     */
    private static String skillBar(models.Skills skill) {
        int pct = (int) Math.min(100, (skill.getProgress() / skill.getRequiredXP()) * 100);
        int filled = pct * BAR_WIDTH / 100;
        int empty = BAR_WIDTH - filled;

        String barColour = pct >= 70 ? GREEN : pct >= 40 ? YELLOW : RED;

        // Pad to 11 — the longest skill name is "Programming"
        String label = GRAY + String.format("%-11s", skill.getSkillName()) + RESET;
        String level = GRAY + String.format("L%-2d", skill.getLevel()) + RESET;
        String bar = GRAY + "[" + RESET
                + barColour + repeat("#", filled) + RESET
                + GRAY + repeat("-", empty) + RESET
                + GRAY + "]" + RESET;

        return label + " " + level + " " + bar;
    }

    // ── Box-drawing primitives ────────────────────────────────────────────────
    /**
     * Prints one body row of the four-column box. Each cell is padded to its
     * column width using {@link #padColoured} so ANSI codes don't corrupt
     * alignment.
     */
    private static void printRow(String l, String m, String s, String n) {
        System.out.println(
                GRAY + "│" + RESET + " " + padColoured(l, LEFT_W)
                + " " + GRAY + "│" + RESET + " " + padColoured(m, MID_W)
                + " " + GRAY + "│" + RESET + " " + padColoured(s, SKILLS_W)
                + " " + GRAY + "│" + RESET + " " + padColoured(n, NOTIF_W)
                + " " + GRAY + "│" + RESET
        );
    }

    // ── Menu item helpers ─────────────────────────────────────────────────────
    /**
     * Returns a bold cyan section title for the middle panel.
     *
     * @param title the title text
     * @return coloured string
     */
    private static String menuTitle(String title) {
        return BOLD + CYAN + title + RESET;
    }

    /**
     * Returns a formatted menu item: number in YELLOW, label in WHITE. Example:
     * {@code "1. Interact Objects"}
     *
     * @param num the option number as a string
     * @param label the descriptive text
     * @return coloured string
     */
    private static String menuItem(String num, String label) {
        return YELLOW + num + ". " + RESET + WHITE + label + RESET;
    }

    /**
     * Returns the standard "0. Back" back-navigation item in GRAY.
     *
     * @return coloured string
     */
    private static String backItem() {
        return GRAY + "0. Back" + RESET;
    }

    // ── ANSI-aware string utilities ───────────────────────────────────────────
    /**
     * Removes all ANSI escape sequences from a string, leaving only printable
     * characters.
     *
     * @param s the input string (may be null)
     * @return the plain string with no escape codes
     */
    private static String stripAnsi(String s) {
        if (s == null) {
            return "";
        }
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    /**
     * Returns the number of visible terminal columns occupied by a string,
     * ignoring any ANSI escape codes it contains.
     *
     * @param s the string to measure
     * @return visible character count
     */
    private static int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    /**
     * Pads a string that may contain ANSI codes to exactly {@code width}
     * visible characters by appending trailing spaces after the raw string
     * (including its colour codes). This ensures the box border character that
     * follows is never accidentally coloured.
     *
     * <p>
     * If the visible content already exceeds {@code width}, colour is stripped
     * and the plain text is hard-truncated to preserve column alignment.
     *
     * @param s the string to pad (may be null)
     * @param width the desired visible width
     * @return a string whose visible length equals {@code width}
     */
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

    /**
     * Centers a possibly-coloured string within {@code width} visible
     * characters, padding with spaces on both sides.
     *
     * @param s the string to center
     * @param width the total visible width to fill
     * @return a string whose visible length equals {@code width}
     */
    private static String centerColoured(String s, int width) {
        int vlen = visibleLength(s);
        if (vlen >= width) {
            return padColoured(s, width);
        }
        int total = width - vlen;
        int lpad = total / 2;
        return repeat(" ", lpad) + s + repeat(" ", total - lpad);
    }

    /**
     * Pads or truncates a plain (no ANSI) string to exactly {@code width}
     * characters.
     *
     * @param s the string to pad (may be null)
     * @param width the desired width
     * @return left-aligned string of exactly {@code width} characters
     */
    private static String pad(String s, int width) {
        if (s == null) {
            s = "";
        }
        if (s.length() > width) {
            s = s.substring(0, width);
        }
        return String.format("%-" + width + "s", s);
    }

    /**
     * Centers a plain (no ANSI) string within {@code width} characters.
     *
     * @param s the string to center
     * @param width the total width to fill
     * @return centered string of exactly {@code width} characters
     */
    private static String center(String s, int width) {
        if (s.length() >= width) {
            return pad(s, width);
        }
        int total = width - s.length();
        int lpad = total / 2;
        return repeat(" ", lpad) + s + repeat(" ", total - lpad);
    }

    /**
     * Returns a string consisting of {@code ch} repeated {@code n} times.
     *
     * @param ch the character or string to repeat
     * @param n the number of repetitions (0 or negative returns empty string)
     * @return the repeated string
     */
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

    // ── Word wrap ─────────────────────────────────────────────────────────────
    /**
     * Splits a plain string into lines of at most {@code width} characters,
     * breaking at word boundaries where possible and hard-breaking at
     * {@code width} if no space is found.
     *
     * <p>
     * Input must not contain ANSI escape codes — strip them first if needed.
     *
     * @param s the plain text to wrap
     * @param width the maximum line length in characters
     * @return a list of lines, each no longer than {@code width} characters
     */
    private static List<String> wordWrap(String s, int width) {
        List<String> result = new ArrayList<>();
        while (s.length() > width) {
            int cut = s.lastIndexOf(' ', width);
            if (cut <= 0) {
                cut = width; // no space found — hard cut

                        }result.add(s.substring(0, cut));
            s = s.substring(cut).stripLeading();
        }
        if (!s.isEmpty()) {
            result.add(s);
        }
        return result;
    }

    // ── Miscellaneous helpers ─────────────────────────────────────────────────
    /**
     * Prints a full-width single-row banner box, used for the create-sim screen
     * header.
     *
     * @param title the text to display centered in the banner
     */
    private static void printBanner(String title) {
        System.out.println(GRAY + "┌" + repeat("─", INNER_W) + "┐" + RESET);
        System.out.println(GRAY + "│" + RESET + center(title, INNER_W) + GRAY + "│" + RESET);
        System.out.println(GRAY + "└" + repeat("─", INNER_W) + "┘" + RESET);
    }

    /**
     * Returns a formatted clock string for the game header, e.g.
     * {@code "DAY 1 - 08:17"}, styled in bold cyan.
     *
     * @param state the current game state
     * @return coloured clock string
     */
    private static String formatClock(GameState state) {
        return BOLD + CYAN
                + "DAY " + state.getGameClock().getDays()
                + " - " + String.format("%02d:%02d",
                        state.getGameClock().getHours(),
                        state.getGameClock().getMinutes())
                + RESET;
    }

    /**
     * Formats a sim's name, age, and gender into a compact display label.
     * Example: {@code "nicholas (21M)"}
     *
     * @param name the sim's name
     * @param age the sim's age as a string
     * @param gender the sim's gender string (only the first character is used)
     * @return formatted label string
     */
    private static String simLabel(String name, String age, String gender) {
        return name + " (" + age + gender.charAt(0) + ")";
    }

    /**
     * Prints the list of sims committed so far during the creation wizard, used
     * as context while the player is entering subsequent sims.
     *
     * @param committed list of {@code [name, age, gender]} string arrays
     */
    private static void showCommitted(List<String[]> committed) {
        if (committed.isEmpty()) {
            return;
        }
        System.out.println("  Sims added so far:");
        for (String[] d : committed) {
            System.out.println("    • " + simLabel(d[0], d[1], d[2]));
        }
        System.out.println();
    }

    /**
     * Clears the terminal using the ANSI escape sequence {@code ESC[H ESC[2J}.
     * Works on any real terminal (Linux, macOS, Windows Terminal).
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
