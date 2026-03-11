package core;

public interface GameState {

    void update();

    void render();

    void handleInput(String input);

}