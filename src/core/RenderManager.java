package core;

public class RenderManager {

  public void clear() {
    System.out.print("\033[H");
  }

  public void flush() {
    System.out.flush();
  }

}