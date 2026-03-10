package core;

public class GameEngine {

    private GameState state;
    private InputBuffer inputBuffer = new InputBuffer();
    private InputManager inputManager = new InputManager(inputBuffer);
    private RenderManager renderer = new RenderManager();
    private boolean running = true;

    public void start() {
        setGameState(new ui.states.CreationState());
        System.out.print("\033[2J");
        while (running) {
            String input = inputManager.pollInput();
            if (input != null) state.handleInput(input, this);
            state.update(this);
            renderer.clear();
            state.render(this);
            drawInputLine();
            renderer.flush();
            sleep(50);
        }
    }

    private void drawInputLine() {
        System.out.print("\033[20;40H");
        System.out.print("> " + inputBuffer.getText());
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) {}
    }

    public void setGameState(GameState state) {
        this.state = state;
        state.onEnter(this);
    }
  
}