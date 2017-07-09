package homewatch.net;

public class ThingResponse {
  private final byte[] payload;
  private final int statusCode;

  public ThingResponse(byte[] payload, int statusCode) {
    this.statusCode = statusCode;
    this.payload = payload;
  }

  public byte[] getPayload() {
    return payload;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
