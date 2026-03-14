package ui.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.needs.*;
import ui.Color;

public class AttributePanel implements Panel {

    private String name;
    private int age;
    private double money;
    private Map<String, Need> needs;

    private static final int BAR_WIDTH = 10;

    public void setCharacter(String name, int age, double money, Map<String, Need> needs) {
        this.name = name;
        this.age = age;
        this.money = money;
        this.needs = needs;
    }

    @Override
    public List<String> render() {

        List<String> lines = new ArrayList<>();

        lines.add(Color.CYAN + name + " | Age " + age + Color.RESET);
        lines.add("─".repeat(40));
        lines.add("");

        for (Map.Entry<String, Need> entry : needs.entrySet()) {

            String label = String.format("%-8s", entry.getKey());
            int percent = (int) entry.getValue().getValue();

            lines.add(label + " " + bar(percent));
        }

        lines.add("");
        lines.add("─".repeat(40));
        lines.add(Color.YELLOW + String.format("Money: $%.2f", money) + Color.RESET);

        return lines;
    }

    private String bar(int percent) {

        int filled = percent * BAR_WIDTH / 100;
        int empty = BAR_WIDTH - filled;

        String bar = "█".repeat(filled) + "░".repeat(empty);

        String color;

        if (percent >= 70) {
            color = Color.GREEN; 
        }else if (percent >= 40) {
            color = Color.YELLOW; 
        }else {
            color = Color.RED;
        }

        return color + bar + Color.RESET + " " + percent + "%";
    }
}
