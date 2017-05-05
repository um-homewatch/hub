package homewatch.exceptions;

public class InvalidSubTypeException extends Exception {
  public InvalidSubTypeException() {
    super("Invalid subtype");
  }

  public InvalidSubTypeException(String message) {
    super(message);
  }
}
