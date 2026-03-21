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
import models.House;
import models.Location;
import models.SimCharacter;
import models.Skills;
import models.actions.Furniture;
import models.actions.FurnitureAction;
import models.needs.Need;
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

    // ── ANSI ──────────────────────────────────────────────────────────────────
    /**
     * ANSI reset — clears all active colour/style attributes.
     */
    private static final String RESET = "\u001B[0m";

    /**
     * ANSI bold text modifier.
     */
    private static final String BOLD = "\u001B[1m";

    /**
     * ANSI white foreground colour.
     */
    private static final String WHITE = "\u001B[37m";

    /**
     * ANSI bright black (dark grey) foreground colour.
     */
    private static final String BRIGHT_BLACK = "\u001B[90m";

    /**
     * ANSI bright red foreground colour.
     */
    private static final String BRIGHT_RED = "\u001B[91m";

    /**
     * ANSI bright green foreground colour.
     */
    private static final String BRIGHT_GREEN = "\u001B[92m";

    /**
     * ANSI bright yellow foreground colour.
     */
    private static final String BRIGHT_YELLOW = "\u001B[93m";

    /**
     * ANSI bright blue foreground colour.
     */
    private static final String BRIGHT_BLUE = "\u001B[94m";

    /**
     * ANSI bright magenta foreground colour.
     */
    private static final String BRIGHT_MAGENTA = "\u001B[95m";

    /**
     * ANSI bright cyan foreground colour.
     */
    private static final String BRIGHT_CYAN = "\u001B[96m";

    /**
     * ANSI bright white foreground colour.
     */
    private static final String BRIGHT_WHITE = "\u001B[97m";

    // ── Semantic aliases ──────────────────────────────────────────────────────
    /**
     * Colour alias used for box borders and dividers.
     */
    private static final String BORDER = BRIGHT_BLACK;

    /**
     * Colour alias used for field labels and UI labels.
     */
    private static final String LABEL = BRIGHT_BLACK;

    /**
     * Colour alias used for secondary / de-emphasised text.
     */
    private static final String MUTED = BRIGHT_BLACK;

    /**
     * Colour alias used for panel and section titles.
     */
    private static final String TITLE = BOLD + BRIGHT_CYAN;

    /**
     * Colour alias used for the in-game clock display.
     */
    private static final String CLOCK = BOLD + BRIGHT_WHITE;

    /**
     * Colour alias used for the active Sim's name.
     */
    private static final String SIM_NAME = BOLD + BRIGHT_WHITE;

    // ── Layout ────────────────────────────────────────────────────────────────
    /**
     * Minimum visible character width for any panel column.
     */
    private static final int MIN_COL_W = 28;

    /**
     * Number of characters used to draw each need/skill progress bar, excluding
     * label and percentage suffix.
     */
    private static final int BAR_WIDTH = 10;

    /**
     * Computed width of the left (stats) panel, updated each render cycle.
     */
    private static int LEFT_W = MIN_COL_W;

    /**
     * Computed width of the middle (actions) panel, updated each render cycle.
     */
    private static int MID_W = MIN_COL_W;

    /**
     * Computed width of the skills panel, updated each render cycle.
     */
    private static int SKILLS_W = MIN_COL_W;

    /**
     * Computed width of the notifications panel, updated each render cycle.
     */
    private static int NOTIF_W = MIN_COL_W;

    /**
     * Total inner width of the combined four-panel box, including inter-column
     * borders. Recalculated each render cycle based on individual panel widths.
     */
    private static int INNER_W = 4 * (MIN_COL_W + 2) + 3;

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

        List<String> stats = buildStatsPanel(player, loc, state, world);
        List<String> actions = buildActionsPanel(step, loc, player, state, world);
        List<String> skills = buildSkillsPanel(player);
        List<String> notifs = buildNotificationsPanel(player);

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

    // ── Stats panel ───────────────────────────────────────────────────────────
    /**
     * Builds the left stats panel showing the active Sim's core attributes.
     *
     * <p>
     * The panel contains, in order:
     * <ol>
     * <li>Sim name, age, and gender initial.</li>
     * <li>Career title and rank, or "Unemployed" if jobless.</li>
     * <li>A colour-coded progress bar for each {@link Need} (green ≥ 70, yellow
     * ≥ 40, red below 40).</li>
     * <li>Current money balance.</li>
     * <li>Current location name.</li>
     * <li>Characters present at the same location with their relationship
     * status and score; NPC descriptions are shown where available.</li>
     * </ol>
     *
     * @param player the active {@link SimCharacter} whose stats are displayed
     * @param loc the {@link Location} the Sim currently occupies
     * @param state the {@link GameState} used to query relationship data
     * @param world the {@link WorldRegistry} (passed through to
     * {@link PlayController#charsAt(Location, GameState, WorldRegistry)})
     * @return an ordered list of ANSI-formatted strings representing the panel
     * rows
     */
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
    /**
     * Builds the centre actions panel containing the context-sensitive menu.
     *
     * <p>
     * The content rendered depends on the current {@link PlayController.Step}:
     * <ul>
     * <li>{@code MAIN} – top-level action menu.</li>
     * <li>{@code INTERACTABLES} – list of {@link Furniture} items at the
     * location.</li>
     * <li>{@code INTERACTABLE_ACTION} – actions available for the selected
     * furniture, including need effects, skill XP gains, monetary cost, and
     * time required.</li>
     * <li>{@code SOCIALISE} – list of characters at the location with their
     * relationship status.</li>
     * <li>{@code SOCIALISE_ACTION} – interaction types available for the
     * selected character.</li>
     * <li>{@code CHANGE_LOCATION} – all world locations, with the current one
     * highlighted.</li>
     * <li>{@code SWITCH_CHARACTER} – all player Sims, with the active one
     * highlighted.</li>
     * <li>{@code PICK_CAREER} – available careers with salary, hours, and
     * related skills.</li>
     * </ul>
     *
     * @param step the current {@link PlayController.Step} determining which
     * sub-menu to display
     * @param loc the {@link Location} the active Sim currently occupies
     * @param player the active {@link SimCharacter}
     * @param state the {@link GameState} providing Sim list and relationship
     * data
     * @param world the {@link WorldRegistry} used to enumerate all locations
     * @return an ordered list of ANSI-formatted strings representing the panel
     * rows
     */
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
                lines.add(menuItem("5", "Shop"));
                lines.add(menuItem("6", "Exit Game"));
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
            case SHOP -> {
                lines.add(menuTitle("Shop"));
                lines.add(menuItem("1", "Browse Houses"));
                lines.add(menuItem("2", "Browse Furniture"));
                lines.add(menuItem("3", "Sell Furniture"));
                lines.add(menuItem("0", "Back to Main Menu"));
            }

            case SHOP_HOUSES -> {
                List<House> houses = PlayController.getCurrentHouses();
                lines.add(menuTitle("Houses for Sale"));
                for (int i = 0; i < houses.size(); i++) {
                    House h = houses.get(i);
                    lines.add(menuItem(String.valueOf(i + 1),
                            h.getLocationName() + " (Tier " + h.getHouseTier() + ") - $" + (int) h.getHousePrice()));
                }
                lines.add(menuItem("0", "Back to Shop"));
            }

            case SHOP_FURNITURE -> {
                List<Furniture> furniture = PlayController.getCurrentFurniture();
                lines.add(menuTitle("Furniture for Sale"));
                for (int i = 0; i < furniture.size(); i++) {
                    Furniture f = furniture.get(i);
                    lines.add(menuItem(String.valueOf(i + 1),
                            f.getName() + " - $" + (int) f.getPrice()));
                }
                lines.add(menuItem("0", "Back to Shop"));
            }

            case SELL_FURNITURE -> {
                List<Furniture> furniture = PlayController.getCurrentFurniture();
                lines.add(menuTitle("Sell Furniture from Your House"));
                for (int i = 0; i < furniture.size(); i++) {
                    Furniture f = furniture.get(i);
                    double refundAmount = f.getPrice() * 0.5;
                    lines.add(menuItem(String.valueOf(i + 1),
                            f.getName() + " - Refund: $" + (int) refundAmount));
                }
                lines.add(menuItem("0", "Back to Shop"));
            }
        }
        return lines;
    }

    // ── Skills panel ──────────────────────────────────────────────────────────
    /**
     * Builds the skills panel showing the active Sim's skill levels as progress
     * bars.
     *
     * <p>
     * Each {@link Skills} entry is rendered as a labelled bar where the fill
     * percentage represents progress towards the next level. Colour thresholds
     * are: green ≥ 70 %, yellow ≥ 40 %, blue below 40 %. The current level is
     * shown as a muted suffix (e.g. {@code Lv3}).
     *
     * @param player the active {@link SimCharacter} whose skills are rendered
     * @return an ordered list of ANSI-formatted strings representing the panel
     * rows
     */
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
    /**
     * Builds the notifications panel displaying recent game events for the
     * active Sim.
     *
     * <p>
     * Notifications are retrieved from {@link NotificationService} and
     * colour-coded by {@link #classifyNotification(String)}. Each notification
     * is word-wrapped to fit the panel width ({@link #NOTIF_W}). Trailing blank
     * lines are trimmed before the list is returned.
     *
     * @param player the active {@link SimCharacter} whose notifications are
     * displayed
     * @return an ordered list of ANSI-formatted strings representing the panel
     * rows
     */
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
    /**
     * Appends effect lines (need deltas or skill XP gains) to an existing line
     * list.
     *
     * <p>
     * The first entry is prefixed with the {@code labelKey} (e.g.
     * {@code "needs"} or {@code "skills"}); subsequent entries are indented to
     * align with the first. Need deltas are coloured green for positive and red
     * for negative values. Skill XP gains are always shown in cyan. Entries are
     * sorted alphabetically by effect name for consistent display.
     *
     * @param lines the line list to append to; must not be {@code null}
     * @param labelKey the display label for the first entry (e.g.
     * {@code "  needs"})
     * @param effects a map of effect name to numeric delta/XP value; may be
     * {@code null} or empty, in which case nothing is added
     * @param isNeeds {@code true} if the effects represent need deltas
     * (colour-coded by sign); {@code false} if they represent skill XP gains
     * (cyan)
     */
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

    /**
     * Renders a labelled, colour-coded progress bar as a single formatted
     * string.
     *
     * <p>
     * The bar uses {@code #} characters for the filled portion and {@code -}
     * for the empty portion, surrounded by muted brackets. The total fill
     * length is always {@link #BAR_WIDTH} characters.
     *
     * <p>
     * Example output: {@code Hunger   [####------]  40%}
     *
     * @param name the label displayed to the left of the bar
     * @param nameWidth the column width reserved for the label (left-padded
     * with spaces)
     * @param value the current value of the stat being represented
     * @param max the maximum possible value; used to compute the fill ratio
     * @param colour the ANSI colour code applied to the filled portion and
     * suffix
     * @param suffix additional text appended after the closing bracket (e.g. a
     * percentage)
     * @return a single ANSI-formatted string representing the complete bar row
     */
    private static String bar(String name, int nameWidth, int value, int max, String colour, String suffix) {
        int filled = value * BAR_WIDTH / max;
        int empty = BAR_WIDTH - filled;
        return LABEL + String.format("%-" + nameWidth + "s", name) + RESET
                + " " + MUTED + "[" + RESET + colour + "#".repeat(filled) + RESET
                + MUTED + "-".repeat(empty) + "]" + RESET
                + " " + colour + suffix + RESET;
    }

    /**
     * Determines the ANSI colour code for a notification message based on
     * keywords.
     *
     * <p>
     * Colour rules (evaluated in priority order):
     * <ul>
     * <li><b>Green</b> – positive events: "levelled up", "improved",
     * "promoted", "earned", "started career".</li>
     * <li><b>Red</b> – negative events: "failed", "starving", "exhausted",
     * "cannot", "not enough".</li>
     * <li><b>Yellow</b> – warning events: "warning", "low", "lonely", "bored",
     * "dirty", "cost".</li>
     * <li><b>White</b> – all other notifications.</li>
     * </ul>
     *
     * @param note the notification text to classify; case-insensitive matching
     * is used
     * @return the ANSI colour escape sequence appropriate for the
     * notification's severity
     */
    private static String classifyNotification(String note) {
        String l = note.toLowerCase();
        if (l.contains("levelled up") || l.contains("improved") || l.contains("promoted")
                || l.contains("earned") || l.contains("started career")) {
            return BRIGHT_GREEN;
        }
        if (l.contains("failed") || l.contains("starving") || l.contains("exhausted")
                || l.contains("cannot") || l.contains("not enough") || l.contains("worsened")) {
            return BRIGHT_RED;
        }
        if (l.contains("warning") || l.contains("low") || l.contains("lonely")
                || l.contains("bored") || l.contains("dirty") || l.contains("cost")) {
            return BRIGHT_YELLOW;
        }
        return BRIGHT_WHITE;
    }

    /**
     * Wraps a menu section title with the {@link #TITLE} colour style.
     *
     * @param t the title text to format
     * @return the title string with ANSI colour codes applied
     */
    private static String menuTitle(String t) {
        return TITLE + t + RESET;
    }

    /**
     * Formats a numbered menu item with a highlighted number and white label.
     *
     * @param n the menu number to display (e.g. {@code "1"})
     * @param l the descriptive label for the menu option
     * @return the formatted menu item string with ANSI colour codes applied
     */
    private static String menuItem(String n, String l) {
        return BRIGHT_YELLOW + n + "." + RESET + " " + WHITE + l + RESET;
    }

    /**
     * Returns a muted "0. Back" string used as the last item in sub-menus.
     *
     * @return the formatted back-navigation menu item
     */
    private static String backItem() {
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

    // ── ANSI / string utilities ───────────────────────────────────────────────
    /**
     * Removes all ANSI escape sequences from a string, returning only the
     * visible text.
     *
     * @param s the string to strip; may be {@code null}
     * @return the plain-text string with all ANSI codes removed, or {@code ""}
     * if {@code s} is {@code null}
     */
    private static String stripAnsi(String s) {
        return s == null ? "" : s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    /**
     * Returns the visible (non-ANSI) character length of a string.
     *
     * @param s the string to measure
     * @return the number of printable characters after stripping ANSI codes
     */
    private static int visibleLength(String s) {
        return stripAnsi(s).length();
    }

    /**
     * Returns the maximum visible length across all strings in a list.
     *
     * <p>
     * Used to calculate dynamic panel column widths before rendering.
     *
     * @param lines the list of ANSI-formatted strings to measure
     * @return the length of the longest visible line, or {@code 0} if the list
     * is empty
     */
    private static int maxVisible(List<String> lines) {
        return lines.stream().mapToInt(Renderer::visibleLength).max().orElse(0);
    }

    /**
     * Pads an ANSI-formatted string to a given visible width.
     *
     * <p>
     * If the visible content of {@code s} exceeds {@code width}, the plain text
     * is truncated and any ANSI codes are discarded. Otherwise, trailing spaces
     * are appended to reach the target width while preserving embedded colour
     * codes.
     *
     * @param s the ANSI-formatted string to pad or truncate; may be
     * {@code null}
     * @param width the target visible character width
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
        return s + " ".repeat(width - plain.length());
    }

    /**
     * Centres an ANSI-formatted string within a given visible width.
     *
     * <p>
     * Leading and trailing spaces are added so that the content appears
     * centred. If the visible length already meets or exceeds {@code width},
     * the string is padded (not truncated) using
     * {@link #padColoured(String, int)}.
     *
     * @param s the ANSI-formatted string to centre
     * @param width the total visible width to fill
     * @return a string of visible length {@code width} with {@code s} centred
     * inside it
     */
    private static String centerColoured(String s, int width) {
        int vlen = visibleLength(s);
        if (vlen >= width) {
            return padColoured(s, width);
        }
        int lpad = (width - vlen) / 2;
        return " ".repeat(lpad) + s + " ".repeat(width - vlen - lpad);
    }

    /**
     * Left-aligns a plain string within a fixed column width, truncating if
     * necessary.
     *
     * @param s the string to pad or truncate; may be {@code null}
     * @param width the target column width in characters
     * @return a string of exactly {@code width} characters
     */
    private static String pad(String s, int width) {
        if (s == null) {
            s = "";
        }
        return String.format("%-" + width + "s", s.length() > width ? s.substring(0, width) : s);
    }

    /**
     * Centres a plain (non-ANSI) string within a fixed column width.
     *
     * <p>
     * If the string length is already equal to or greater than {@code width},
     * it is left-aligned via {@link #pad(String, int)}.
     *
     * @param s the plain string to centre
     * @param width the total column width in characters
     * @return a string of exactly {@code width} characters with {@code s}
     * centred
     */
    private static String center(String s, int width) {
        if (s.length() >= width) {
            return pad(s, width);
        }
        int lpad = (width - s.length()) / 2;
        return " ".repeat(lpad) + s + " ".repeat(width - s.length() - lpad);
    }

    /**
     * Wraps a plain string into a list of lines, each no longer than
     * {@code width} characters.
     *
     * <p>
     * Breaks are made at the last space within the limit where possible; if no
     * space is found, a hard break is made at {@code width}. Leading whitespace
     * is stripped from the start of each continuation line.
     *
     * @param s the string to wrap; must not be {@code null}
     * @param width the maximum visible line width in characters
     * @return an ordered list of substrings, each within {@code width}
     * characters; the list will have at least one element if {@code s} is
     * non-empty
     */
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

    /**
     * Formats a duration given in fractional hours as a human-readable string.
     *
     * <p>
     * Examples:
     * <ul>
     * <li>{@code 0.5}  → {@code "30min"}</li>
     * <li>{@code 2.0}  → {@code "2h"}</li>
     * <li>{@code 1.5}  → {@code "1h 30min"}</li>
     * </ul>
     *
     * @param h the duration in hours; values less than {@code 1.0} are shown in
     * minutes only
     * @return a formatted duration string
     */
    private static String formatHours(double h) {
        if (h < 1.0) {
            return (int) (h * 60) + "min";
        }
        int hrs = (int) h, mins = (int) Math.round((h - hrs) * 60);
        return mins > 0 ? hrs + "h " + mins + "min" : hrs + "h";
    }

    /**
     * Clears the terminal screen using the standard ANSI escape sequence
     * ({@code ESC[H ESC[2J}) and flushes {@code System.out}.
     *
     * <p>
     * This method is called at the start of every render cycle to avoid output
     * accumulation between frames.
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
