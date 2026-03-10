package ui.character.bodies;

import ui.character.animation.Animation;

public class IdleBody implements Animation {
    public static final String[][] FRAMES = {
        {
            "        |",
            "      \\ | /",
            "       \\|/",
            "        |",
            "       / \\",
            "      /   \\"
        },
        {
            "        |",
            "      \\ | /",
            "       \\|/",
            "        |",
            "       /|",
            "      / |"
        },
        {
            "        |",
            "      \\ | /",
            "       \\|/",
            "        |",
            "       / \\",
            "      /   \\"
        },
        {
            "        |",
            "      \\ | /",
            "       \\|/",
            "        |",
            "       |\\",
            "       | \\"
        }
    };
    public String[][] frames() {
        return FRAMES;
    }
}