package ui.panel;

import java.util.ArrayList;
import java.util.List;

public class CreateSimPanel implements Panel {

    private final List<String[]> sims = new ArrayList<>();

    private String name = "";
    private String age = "";
    private String gender = "";
    private int maxSims = 1;

    private boolean showOptions = false;

    public void setMaxSims(int max) {
        this.maxSims = max;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void commitSim() {
        sims.add(new String[] { name, age, gender });
        name = "";
        age = "";
        gender = "";
    }

    public void showOptions(boolean value) {
        showOptions = value;
    }

    public void reset() {
        sims.clear();
        name = "";
        age = "";
        gender = "";
        showOptions = false;
    }

    public List<String[]> getSims() {
        return sims;
    }

    @Override
    public List<String> render() {

        List<String> lines = new ArrayList<>();

        lines.add("Create Your Sim");
        lines.add("");

        // completed sims
        for (int i = 0; i < sims.size(); i++) {

            String[] sim = sims.get(i);

            lines.add("Sim " + (i + 1));
            lines.add("Name: " + sim[0]);
            lines.add("Age: " + sim[1]);
            lines.add("Gender: " + sim[2]);
            lines.add("");
        }

        // current sim being edited
        if (!showOptions && sims.size() < maxSims) {

            int current = sims.size() + 1;

            lines.add("Sim " + current);
            lines.add("Name: " + name);
            lines.add("Age: " + age);
            lines.add("Gender: " + gender);
            lines.add("");
        }

        // options
        if (showOptions) {

            lines.add("Options");
            lines.add("1. Keep changes");
            lines.add("2. Edit character");
            lines.add("3. Reset all");
        }

        return lines;
    }

}