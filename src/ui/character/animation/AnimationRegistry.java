package ui.character.animation;

import ui.character.bodies.*;
import ui.character.faces.*;
import java.util.Map;

public class AnimationRegistry {
    private static final Map<String, Animation> FACE_MAP = Map.of(
        "Neutral", new NeutralFace()
        // TODO: fix mood such that it links with face
    );
    private static final Map<String, Animation> BODY_MAP = Map.of(
        "Idle", new IdleBody()
        // TODO: fix activities such that it links with body
    );
    public static Animation getFace(String mood) {
        return FACE_MAP.get(mood);
        // TODO: fix mood to a enum or smth
    }
    public static Animation getBody(String activity) {
        return BODY_MAP.get(activity);
        // TODO: fix activity to a enum or smth
    }

}