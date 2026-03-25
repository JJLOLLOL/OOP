package ui;

import core.GameState;
import core.WorldRegistry;
import ui.views.CreateSimView;
import ui.views.GameplayView;

import static ui.ConsoleUtils.*;

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
            case CREATE_SIM -> CreateSimView.render(state);
            case PLAYING -> GameplayView.render(state, world);
            case QUIT -> {}
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
}
