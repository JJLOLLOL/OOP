package ui.panel;

import java.util.ArrayList;
import java.util.List;
import models.SimCharacter;

/**
 * RIGHT panel — shows the active action area: - current sim being entered /
 * edited - keep/delete options after GENDER entry - edit/delete options after
 * selecting a sim - pick active player list
 */
public class CreateSimActionPanel implements Panel {

    public enum Mode {
        ENTERING, // entering / editing a sim's fields
        OPTIONS, // keep(1) / delete(2) after GENDER entry
        SELECT_ACTION, // edit(1) / delete(2) for a chosen committed sim
        PICK_PLAYER, // choose which sim becomes the active player
        EMPTY           // no action in progress (e.g. SELECT step waiting for number)
    }

    private Mode mode = Mode.EMPTY;
    private int simNumber = 0;    // 1-based label for the sim being entered/edited
    private int selectedSim = -1;   // 1-based label for SELECT_ACTION header

    // fields shown during ENTERING mode
    private String name = "";
    private String age = "";
    private String gender = "";

    // data shown during PICK_PLAYER mode
    private List<SimCharacter> pickList = new ArrayList<>();

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setSimNumber(int n) {
        this.simNumber = n;
    }

    public void setSelectedSim(int index) {
        this.selectedSim = index;
    } // 0-based, displayed as +1

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPickList(List<SimCharacter> list) {
        this.pickList = list;
    }

    public void reset() {
        mode = Mode.EMPTY;
        simNumber = 0;
        selectedSim = -1;
        name = "";
        age = "";
        gender = "";
        pickList = new ArrayList<>();
    }

    // ── Render ────────────────────────────────────────────────────────────────
    @Override
    public List<String> render() {
        List<String> lines = new ArrayList<>();

        switch (mode) {

            case ENTERING -> {
                lines.add("Sim " + simNumber);
                lines.add("");
                lines.add("  Name:   " + name);
                lines.add("  Age:    " + age);
                lines.add("  Gender: " + gender);
            }

            case OPTIONS -> {
                lines.add("Sim " + simNumber);
                lines.add("");
                lines.add("  Name:   " + name);
                lines.add("  Age:    " + age);
                lines.add("  Gender: " + gender);
                lines.add("");
                lines.add("Options");
                lines.add("  1. Keep");
                lines.add("  2. Delete");
            }

            case SELECT_ACTION -> {
                lines.add("Options for Sim " + (selectedSim + 1));
                lines.add("");
                lines.add("  1. Edit");
                lines.add("  2. Delete");
            }

            case PICK_PLAYER -> {
                lines.add("Who will you play as?");
                lines.add("");
                for (int i = 0; i < pickList.size(); i++) {
                    SimCharacter s = pickList.get(i);
                    lines.add("  " + (i + 1) + ". " + s.getName());
                }
            }

            case EMPTY -> {
            }
        }

        return lines;
    }
}
