package homewatch.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

class ErrorMessage {
  @JsonIgnore
  private final Exception e;

  ErrorMessage(Exception e) {
    this.e = e;
  }

  public String getMessage() {
    return e.getMessage();
  }
}
