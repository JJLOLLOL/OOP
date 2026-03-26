package ui.panels;

import Types.InteractionList;
import Types.RelationshipList;
import core.GameState;
import core.PlayController;
import core.WorldRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.career.CareerList;
import models.character.SimCharacter;
import models.furniture.Furniture;
import models.furniture.FurnitureAction;
import models.location.House;
import models.location.Location;
import models.skill.SkillType;

import static ui.ConsoleUtils.*;
import static ui.Renderer.*;

/**
 * Renders the middle "Actions" panel of the gameplay UI.
 */
public class ActionsPanelView {

    /**
     * Builds the centre actions panel containing the context-sensitive menu.
     *
     * @param step the current {@link PlayController.Step} determining which sub-menu to display
     * @param loc the {@link Location} the active Sim currently occupies
     * @param player the active {@link SimCharacter}
     * @param state the {@link GameState} providing Sim list and relationship data
     * @param world the {@link WorldRegistry} used to enumerate all locations
     * @return an ordered list of ANSI-formatted strings representing the panel rows
     */
    public static List<String> build(PlayController.Step step, Location loc,
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
                List<Furniture> flist = loc.getFurnitureViews();
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
                List<models.character.Character> chars = PlayController.charsAt(loc, state, world);
                if (chars.isEmpty()) {
                    lines.add(MUTED + "Nobody here." + RESET);
                } else {
                    for (int i = 0; i < chars.size(); i++) {
                        RelationshipList status = player.getRelationshipStatus(chars.get(i));
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
            
                int indexWidth = String.valueOf(careers.size()).length() + 2; // e.g. "12."
                int titleWidth = careers.stream()
                        .mapToInt(c -> c.getTitle().length())
                        .max()
                        .orElse(10) + 2;
            
                String headerIndent = "  ";
                String rowPrefix = headerIndent
                        + pad("", indexWidth) + " "
                        + pad("", titleWidth) + " "
                        + pad("", 9) + "  "
                        + pad("", 5) + "  ";
            
                lines.add(MUTED + headerIndent
                    + pad("", indexWidth)
                    + pad("Career", titleWidth) + " "
                    + pad("Salary", 9) + " "
                    + pad("Hours", 6) + " "
                    + "Skills" + RESET);
            
                lines.add(MUTED + "    " + "─".repeat(rowPrefix.length() - headerIndent.length() + "Skills".length()) + RESET);
            
                for (int i = 0; i < careers.size(); i++) {
                    CareerList career = careers.get(i);
            
                    String numberText = pad((i + 1) + ".", indexWidth);
                    String salaryText = String.format("$%.0f/d", career.getBaseSalary());
                    String hoursText = career.getWorkingHours() > 0
                            ? (int) career.getWorkingHours() + "h"
                            : "";
            
                    SkillType[] skills = career.getRelatedSkills();
            
                    for (int j = 0; j < skills.length; j++) {
                        String skillText = skills[j].getName();
            
                        if (j == 0) {
                            lines.add(BRIGHT_YELLOW + numberText + RESET + " "
                                    + BRIGHT_WHITE + pad(career.getTitle(), titleWidth) + RESET
                                    + "  " + MUTED + pad(salaryText, 9) + RESET
                                    + "  " + MUTED + pad(hoursText, 5) + RESET
                                    + "  " + BRIGHT_BLACK + skillText + RESET);
                        } else {
                            lines.add(" ".repeat(indexWidth + 1)
                                    + pad("", titleWidth)
                                    + "  " + pad("", 9)
                                    + "  " + pad("", 5)
                                    + "  " + BRIGHT_BLACK + skillText + RESET);
                        }
                    }
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
                            h.getLocationName() + " (Tier " + h.getTier() + ") - $" + (int) h.getPrice()));
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
                    String formattedRefund = String.format("%.2f", refundAmount);
                    lines.add(menuItem(String.valueOf(i + 1),
                            f.getName() + " - Refund: $" + formattedRefund));
                }
                lines.add(menuItem("0", "Back to Shop"));
            }
        }
        return lines;
    }

    /**
     * Appends effect lines (need deltas or skill XP gains) to an existing line list.
     */
    private static <E extends Enum<E>> void addEffectLines(
        List<String> lines,
        String labelKey,
        Map<E, Double> effects,
        boolean isNeeds) {

    if (effects == null || effects.isEmpty()) {
        return;
    }

    boolean first = true;
    for (Map.Entry<E, Double> e : new TreeMap<>(effects).entrySet()) {
        double v = e.getValue();
        String prefix = first ? MUTED + labelKey + ": " + RESET : "         ";
        String effectName = e.getKey().toString();

        String value = isNeeds
                ? (v > 0 ? BRIGHT_GREEN : BRIGHT_RED) + (v > 0 ? "+" : "") + (int) v + " " + effectName + RESET
                : BRIGHT_CYAN + "+" + (int) v + "xp " + effectName + RESET;

        lines.add(prefix + value);
        first = false;
    }
}
}