package core;

public class InputManager {

  private InputBuffer buffer;

  public InputManager(InputBuffer buffer) {
    this.buffer = buffer;
  }
  
  public String pollInput() {
    try {
      while (System.in.available() > 0) {
        char c = (char) System.in.read();
        if (c == '\n') return buffer.submit();
        if (c == 127)
          buffer.backspace();
        else
          buffer.type(c);
      }
    } catch (Exception ignored) {}
    return null;
  }

}