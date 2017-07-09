package homewatch.things;

import java.util.Map;

public abstract class NetworkThingService<T extends Thing> extends ThingService<T> {
  private String address;
  private Integer port = null;

  NetworkThingService() {
  }

  NetworkThingService(String address) {
    this.address = address;
  }

  NetworkThingService(String address, Integer port) {
    this.address = address;
    this.port = port;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  @Override
  public void setAttributes(Map<String, ?> attributes) {
    String addressString = this.getAttribute(attributes, "address", String.class);
    String portString = this.getAttribute(attributes, "port", String.class);

    if (addressString == null) {
      throw new IllegalArgumentException("missing address");
    }

    try {
      this.address = addressString;
      this.port = (portString == null) ? null : Integer.parseInt(portString);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("invalid port");
    }
  }

  protected abstract String getUrl();
}
