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

import static ui.ConsoleUtils.*;

import models.actions.Furniture;
import models.actions.FurnitureAction;
import models.character.SimCharacter;
import models.location.House;
import models.location.Location;
import models.need.Need;
import models.skill.Skill;
import ui.panels.ActionsPanelView;
import ui.panels.NotificationsPanelView;
import ui.panels.SkillsPanelView;
import ui.panels.StatsPanelView;
import services.NotificationService;

/**
 * Responsible for all CLI rendering in the Sims simulation game.
 *
 * <p>
 * The {@code Renderer} class is the sole presentation layer of the system. It
 * reads from {@link GameState} and {@link WorldRegistry} and prints formatted,
 * ANSI-coloured output to {@code System.out}. The class is entirely static; no
 * instances are created.
 *
 * <p>
 * The display is divided into four side-by-side panels during gameplay:
 * <ol>
 * <li><b>Stats</b> – active Sim's name, career, needs bars, money, and nearby
 * characters.</li>
 * <li><b>Actions</b> – context-sensitive menu driven by
 * {@link PlayController.Step}.</li>
 * <li><b>Skills</b> – skill progress bars for the active Sim.</li>
 * <li><b>Notifications</b> – recent event messages colour-coded by
 * severity.</li>
 * </ol>
 *
 * <p>
 * During Sim creation, a separate creation-flow screen is shown instead.
 *
 * <p>
 * <b>ANSI colour support:</b> All colour constants use standard ANSI escape
 * sequences. Output may appear unstyled in terminals that do not support ANSI
 * codes.
 *
 * @see GameState
 * @see WorldRegistry
 * @see PlayController
 * @see CreateSimController
 */
public class Renderer {

    // ── Semantic aliases ──────────────────────────────────────────────────────
    /**
     * Colour alias used for box borders and dividers.
     */
    public static final String BORDER = BRIGHT_BLACK;

    /**
     * Colour alias used for field labels and UI labels.
     */
    public static final String LABEL = BRIGHT_BLACK;

    /**
     * Colour alias used for secondary / de-emphasised text.
     */
    public static final String MUTED = BRIGHT_BLACK;

    /**
     * Colour alias used for panel and section titles.
     */
    public static final String TITLE = BOLD + BRIGHT_CYAN;

    /**
     * Colour alias used for the in-game clock display.
     */
    public static final String CLOCK = BOLD + BRIGHT_WHITE;

    /**
     * Colour alias used for the active Sim's name.
     */
    public static final String SIM_NAME = BOLD + BRIGHT_WHITE;

    // ── Layout ────────────────────────────────────────────────────────────────
    /**
     * Minimum visible character width for any panel column.
     */
    public static final int MIN_COL_W = 28;

    /**
     * Number of characters used to draw each need/skill progress bar, excluding
     * label and percentage suffix.
     */
    public static final int BAR_WIDTH = 10;

    /**
     * Computed width of the left (stats) panel, updated each render cycle.
     */
    public static int LEFT_W = MIN_COL_W;

    /**
     * Computed width of the middle (actions) panel, updated each render cycle.
     */
    public static int MID_W = MIN_COL_W;

    /**
     * Computed width of the skills panel, updated each render cycle.
     */
    public static int SKILLS_W = MIN_COL_W;

    /**
     * Computed width of the notifications panel, updated each render cycle.
     */
    public static int NOTIF_W = MIN_COL_W;

    /**
     * Total inner width of the combined four-panel box, including inter-column
     * borders. Recalculated each render cycle based on individual panel widths.
     */
    public static int INNER_W = 4 * (MIN_COL_W + 2) + 3;

    // ── Public API ────────────────────────────────────────────────────────────
    /**
     * Clears the terminal and renders the appropriate screen for the current
     * game phase.
     *
     * <p>
     * Delegates to {@link #renderCreateSim(GameState)} during Sim creation and
     * to {@link #renderPlaying(GameState, WorldRegistry)} during active
     * gameplay. The {@code QUIT} phase produces no output.
     *
     * @param state the current {@link GameState}, used to determine the phase
     * and retrieve Sim data
     * @param world the {@link WorldRegistry} providing location information
     * used during the playing phase
     */
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

    /**
     * Prints a formatted error message to the console and re-displays the
     * command prompt {@code >}.
     *
     * <p>
     * The message is prefixed with a bright-red {@code [!]} indicator so that
     * errors are visually distinct from normal output.
     *
     * @param message the error message to display; should be non-null
     */
    public static void showError(String message) {
        System.out.println("  " + BRIGHT_RED + "[!] " + RESET + WHITE + message + RESET);
        System.out.print("> ");
    }

    // ── CREATE SIM ────────────────────────────────────────────────────────────
    /**
     * Renders the Sim-creation wizard screen.
     *
     * <p>
     * The content varies based on {@link CreateSimController#getStep()}:
     * <ul>
     * <li>{@code COUNT} – asks how many Sims to create.</li>
     * <li>{@code NAME} – shows already-committed Sims and prompts for a
     * name.</li>
     * <li>{@code AGE} – shows in-flight name and prompts for an age.</li>
     * <li>{@code GENDER}– shows in-flight name and age, prompts for
     * gender.</li>
     * <li>{@code CONFIRM} – lists all Sims for final review with a Y/N
     * prompt.</li>
     * <li>{@code PICK_PLAYER} – lets the player choose their active Sim.</li>
     * </ul>
     *
     * @param state the current {@link GameState}; used to retrieve the list of
     * created Sims during the {@code PICK_PLAYER} step
     */
    private static void renderCreateSim(GameState state) {
        printBanner("CREATE YOUR SIMS");
        System.out.println();
        switch (CreateSimController.getStep()) {
            case COUNT ->
                prompt("How many Sims do you want to create?");
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
    /**
     * Renders the main gameplay screen as a four-panel bordered box.
     *
     * <p>
     * Each panel is assembled into a {@link List} of ANSI-coloured strings by a
     * dedicated {@code build*Panel} method. Column widths are dynamically
     * calculated from the maximum visible (non-ANSI) line length in each panel
     * so that the box never wraps or truncates content. The header row shows
     * the current in-game day and time, and a {@code >} prompt is printed after
     * the box.
     *
     * @param state the current {@link GameState}, providing the active Sim,
     * clock, and relationship data
     * @param world the {@link WorldRegistry}, used to enumerate available
     * locations and characters at the current location
     */
    private static void renderPlaying(GameState state, WorldRegistry world) {
        SimCharacter player = state.getActivePlayer();
        Location loc = player.getLocation();
        PlayController.Step step = PlayController.getStep();

        List<String> stats = StatsPanelView.build(player, loc, state, world);
        List<String> actions = ActionsPanelView.build(step, loc, player, state, world);
        List<String> skills = SkillsPanelView.build(player);
        List<String> notifs = NotificationsPanelView.build(player);

        LEFT_W = Math.max(MIN_COL_W, maxVisible(stats));
        MID_W = Math.max(MIN_COL_W, maxVisible(actions));
        SKILLS_W = Math.max(MIN_COL_W, maxVisible(skills));
        NOTIF_W = Math.max(MIN_COL_W, maxVisible(notifs));
        INNER_W = (LEFT_W + 2) + (MID_W + 2) + (SKILLS_W + 2) + (NOTIF_W + 2) + 3;

        printBoxTop(CLOCK + "DAY " + state.getGameClock().getDays() + "  ─  "
                + String.format("%02d:%02d", state.getGameClock().getHours(), state.getGameClock().getMinutes()) + RESET);

        System.out.println(BORDER + "├" + seg(LEFT_W + 2) + "┬" + seg(MID_W + 2) + "┬" + seg(SKILLS_W + 2)
                + "┬" + seg(NOTIF_W + 2) + "┤" + RESET);

        int rows = Math.max(Math.max(stats.size(), actions.size()), Math.max(skills.size(), notifs.size()));
        for (int i = 0; i < rows; i++) {
            System.out.println(
                    BORDER + "│" + RESET + " " + padColoured(get(stats, i), LEFT_W)
                    + " " + BORDER + "│" + RESET + " " + padColoured(get(actions, i), MID_W)
                    + " " + BORDER + "│" + RESET + " " + padColoured(get(skills, i), SKILLS_W)
                    + " " + BORDER + "│" + RESET + " " + padColoured(get(notifs, i), NOTIF_W)
                    + " " + BORDER + "│" + RESET);
        }

        System.out.println(BORDER + "└" + seg(LEFT_W + 2) + "┴" + seg(MID_W + 2) + "┴" + seg(SKILLS_W + 2)
                + "┴" + seg(NOTIF_W + 2) + "┘" + RESET);
        System.out.print("\n> ");
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    /**
     * Wraps a menu section title with the {@link #TITLE} colour style.
     *
     * @param t the title text to format
     * @return the title string with ANSI colour codes applied
     */
    public static String menuTitle(String t) {
        return TITLE + t + RESET;
    }

    /**
     * Formats a numbered menu item with a highlighted number and white label.
     *
     * @param n the menu number to display (e.g. {@code "1"})
     * @param l the descriptive label for the menu option
     * @return the formatted menu item string with ANSI colour codes applied
     */
    public static String menuItem(String n, String l) {
        return BRIGHT_YELLOW + n + "." + RESET + " " + WHITE + l + RESET;
    }

    /**
     * Returns a muted "0. Back" string used as the last item in sub-menus.
     *
     * @return the formatted back-navigation menu item
     */
    public static String backItem() {
        return MUTED + "0. Back" + RESET;
    }

    /**
     * Safely retrieves an element from a list by index, returning an empty
     * string if the index is out of bounds.
     *
     * <p>
     * Used when rendering panel rows: all four panels may have different
     * heights, so shorter panels return empty strings for the extra rows.
     *
     * @param list the list to retrieve from
     * @param i the zero-based index to access
     * @return the element at index {@code i}, or {@code ""} if
     * {@code i >= list.size()}
     */
    private static String get(List<String> list, int i) {
        return i < list.size() ? list.get(i) : "";
    }

    /**
     * Returns a horizontal line segment of {@code n} box-drawing dash
     * characters ({@code ─}).
     *
     * @param n the number of characters in the segment; must be non-negative
     * @return a string of {@code n} repeated {@code ─} characters
     */
    private static String seg(int n) {
        return "─".repeat(n);
    }

    /**
     * Prints a muted label prompt line to {@code System.out}.
     *
     * @param text the prompt text to display; should be non-null
     */
    private static void prompt(String text) {
        System.out.println("  " + LABEL + text + RESET);
    }

    /**
     * Prints a key-value field line to {@code System.out}, used during the
     * Sim-creation wizard to show already-entered values.
     *
     * @param key the field label (e.g. {@code "Name"}), left-padded to 4
     * characters
     * @param val the field value to display in bright white
     */
    private static void field(String key, String val) {
        System.out.println("  " + MUTED + pad(key, 4) + " : " + RESET + BRIGHT_WHITE + val + RESET);
    }

    /**
     * Prints the top border of the main gameplay box, including the centred
     * clock line.
     *
     * <p>
     * The box uses Unicode box-drawing characters and is sized to
     * {@link #INNER_W}.
     *
     * @param clock the pre-formatted, ANSI-coloured clock string to centre in
     * the header
     */
    private static void printBoxTop(String clock) {
        System.out.println(BORDER + "┌" + seg(INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + centerColoured(clock, INNER_W) + BORDER + "│" + RESET);
    }

    /**
     * Prints a full single-line banner box with a centred title, used for the
     * Sim-creation screen heading.
     *
     * @param title the title text to centre and display inside the banner box
     */
    private static void printBanner(String title) {
        System.out.println(BORDER + "┌" + seg(INNER_W) + "┐" + RESET);
        System.out.println(BORDER + "│" + RESET + CLOCK + center(title, INNER_W) + RESET + BORDER + "│" + RESET);
        System.out.println(BORDER + "└" + seg(INNER_W) + "┘" + RESET);
    }

    /**
     * Formats a Sim's identifying information as a single display label.
     *
     * <p>
     * Example: {@code Alice (25F)}
     *
     * @param name the Sim's name
     * @param age the Sim's age as a string
     * @param gender the Sim's gender string; only the first character is used
     * @return an ANSI-formatted label combining name, age, and gender initial
     */
    private static String simLabel(String name, String age, String gender) {
        return BRIGHT_WHITE + name + RESET + MUTED + " (" + age + gender.charAt(0) + ")" + RESET;
    }

    /**
     * Prints the list of Sims committed so far during the creation wizard.
     *
     * <p>
     * Each Sim is shown as a bulleted line with name, age, and gender. If the
     * committed list is empty, nothing is printed.
     *
     * @param committed a list of {@code String[]} arrays where each array
     * contains {@code [name, age, gender]} for one committed Sim
     */
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

}
