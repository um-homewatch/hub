package exceptions;

public class NetworkException extends Exception {
  private final int statusCode;

  public NetworkException(int statusCode) {
    this.statusCode = statusCode;
  }

  public NetworkException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public NetworkException(String message, Throwable cause, int statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public NetworkException(Throwable cause, int statusCode) {
    super(cause);
    this.statusCode = statusCode;
  }

  public NetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int statusCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
