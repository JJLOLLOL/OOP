package core;

public class InputBuffer {

  private StringBuilder buffer = new StringBuilder();
  private boolean submitted = false;

  public void type(char c) {
    buffer.append(c);
  }

  public void backspace() {
    if (buffer.length() > 0)
      buffer.deleteCharAt(buffer.length() - 1);
  }

  public String submit() {
    submitted = true;
    String text = buffer.toString();
    buffer.setLength(0);
    return text;
  }

  public String getText() {
    return buffer.toString();
  }

  public boolean isSubmitted() {
    return submitted;
  }

  public void resetSubmit() {
    submitted = false;
  }

}