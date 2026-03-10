package ui.character.renderer;

import ui.character.animation.Animation;
import ui.character.animation.AnimationRegistry;

public class CharacterRenderer {

    private int frame = 0;

    public void update() {
        frame = (frame + 1) % 4;
    }


    public String[] render(String mood, String activity) {

        Animation faceAnim = AnimationRegistry.getFace(mood);
        Animation bodyAnim = AnimationRegistry.getBody(activity);

        String[] face = faceAnim.frames()[frame];
        String[] body = bodyAnim.frames()[frame];

        frame = (frame + 1) % faceAnim.frames().length;
        return CharacterForm.form(face, body);
    }
}