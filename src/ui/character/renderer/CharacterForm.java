package ui.character.renderer;

public class CharacterForm {
    public static String[] form(String[] face, String[] body) {
        String[] result = new String[face.length + body.length];
        for (int i = 0; i < face.length; i++)
            result[i] = face[i];
        for (int i = 0; i < body.length; i++)
            result[i + face.length] = body[i];
        return result;
    }
}