package ui.panel;

import Types.InteractionType;
import java.util.ArrayList;
import java.util.List;

public class SocialisePanel implements Panel {

    private List<models.Character> characters = new ArrayList<>();
    private models.Character selectedCharacter = null;

    public void setCharacters(List<models.Character> characters) {
        if (!characters.equals(this.characters)) {
            this.characters = characters;
            this.selectedCharacter = null; // only reset when list changes
        }
    }

    public void selectCharacter(models.Character character) {
        this.selectedCharacter = character;
    }

    public void clearSelection() {
        this.selectedCharacter = null;
    }

    @Override
    public List<String> render() {
        return selectedCharacter == null ? renderCharacterList() : renderInteractionList();
    }

    private List<String> renderCharacterList() {
        List<String> lines = new ArrayList<>();
        lines.add("Socialise");
        lines.add("");

        if (characters.isEmpty()) {
            lines.add("No one around to socialise with.");
        } else {
            for (int i = 0; i < characters.size(); i++) {
                lines.add((i + 1) + ". " + characters.get(i).getName());
            }
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }

    private List<String> renderInteractionList() {
        List<String> lines = new ArrayList<>();
        lines.add(selectedCharacter.getName());
        lines.add("");

        InteractionType[] types = InteractionType.values();
        for (int i = 0; i < types.length; i++) {
            lines.add((i + 1) + ". " + types[i].getLabel());
        }

        lines.add("");
        lines.add("0. Back");
        return lines;
    }
}
