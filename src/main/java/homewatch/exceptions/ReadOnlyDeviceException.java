package homewatch.exceptions;

public class ReadOnlyDeviceException extends RuntimeException {
  public ReadOnlyDeviceException() {
  }

  public ReadOnlyDeviceException(String message) {
    super(message);
  }

  public ReadOnlyDeviceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReadOnlyDeviceException(Throwable cause) {
    super(cause);
  }

  public ReadOnlyDeviceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
