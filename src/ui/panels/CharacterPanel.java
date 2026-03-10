package ui.panels;

import ui.character.renderer.CharacterRenderer;
import core.GameEngine;

public class CharacterPanel extends Panel {

    private CharacterRenderer renderer = new CharacterRenderer();

    public CharacterPanel(int row, int col) {
        super(row, col);
        this.renderer = new CharacterRenderer();
    }
    
    public void update() {
        renderer.update();
    }
    public void render(GameEngine engine) {

        // TODO: implement getting mood and activity from game engine
        // Mood mood = engine.getActivePlayer().getMood();
        // Activity activity = engine.getActivePlayer().getActivity();

        String[] frame = renderer.render("Neutral", "Idle");

        for (int i = 0; i < frame.length; i++) {
        moveCursor(row + i, col);
        System.out.print(frame[i]);
        }
    }
}