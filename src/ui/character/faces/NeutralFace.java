package ui.character.faces;

import ui.character.animation.Animation;

public class NeutralFace implements Animation {
    public static final String[][] FRAMES = {
        {
            "     _______",
            "    /       \\",
            "   |  ^   ^  |",
            "   |  \\___/  |",
            "    \\_______/"
        },
        {
            "     _______",
            "    /       \\",
            "   |  -   -  |",
            "   |  \\___/  |",
            "    \\_______/"
        },
        {
            "     _______",
            "    /       \\",
            "   |  ^   ^  |",
            "   |  \\___/  |",
            "    \\_______/"
        },
        {
            "     _______",
            "    /       \\",
            "   |  o   o  |",
            "   |  \\___/  |",
            "    \\_______/"
        }
    };
    public String[][] frames() {
        return FRAMES;
    }
}