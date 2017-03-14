package things.exceptions;

public class InvalidSubTypeException extends Exception {
  public InvalidSubTypeException() {
    super();
  }

  public InvalidSubTypeException(String message) {
    super(message);
  }
}
