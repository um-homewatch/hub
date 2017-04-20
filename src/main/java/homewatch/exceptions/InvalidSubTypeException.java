package homewatch.exceptions;

public class InvalidSubTypeException extends Exception {
  public InvalidSubTypeException() {
    super("Invalid subType");
  }

  public InvalidSubTypeException(String message) {
    super(message);
  }
}
