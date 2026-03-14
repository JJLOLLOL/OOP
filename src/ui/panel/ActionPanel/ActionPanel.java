package ui.panel;

import java.util.ArrayList;
import java.util.List;

public class ActionPanel implements Panel {

  private ActionMenuMode mode;

  public ActionPanel(ActionMenuMode mode) {
    this.mode = mode;
  }

  @Override
  public List<String> render() {

    List<String> lines = new ArrayList<>();

    switch (mode) {

      case MAIN:
        lines.add("Actions");
        lines.add("");
        lines.add("1. Interactables");
        lines.add("2. Socialise");
        lines.add("3. Change Location");
        lines.add("4. Exit Game");
        break;

      case INTERACTABLES:
        lines.add("Interactables");
        lines.add("");
        lines.add("1. Bed");
        lines.add("   ├── Sleep");
        lines.add("   └── Nap");
        lines.add("");
        lines.add("2. Stove");
        lines.add("   ├── Cook");
        lines.add("   └── Grill");
        lines.add("");
        lines.add("0. Back");
        break;

      case SOCIALISE:
        lines.add("Socialise");
        lines.add("");
        lines.add("1. Talk to Alice");
        lines.add("2. Compliment Alice");
        lines.add("3. Insult Alice");
        lines.add("");
        lines.add("0. Back");
        break;

      case CHANGE_LOCATION:
        lines.add("Change Location");
        lines.add("");
        lines.add("1. Home");
        lines.add("2. Cafe");
        lines.add("3. Park");
        lines.add("");
        lines.add("0. Back");
        break;
    }

    return lines;
  }
}