package core;

public interface GameState {
  
  void onEnter(GameEngine engine);

  void update(GameEngine engine);
  
  void render(GameEngine engine);
  
  void handleInput(String input, GameEngine engine);
  
}