package homewatch.things;

import java.util.Map;

public abstract class NetworkThingService<T> extends ThingService<T> {
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
    String address = this.getAttribute(attributes, "address", String.class);
    String port = this.getAttribute(attributes, "port", String.class);

    if (address == null) {
      throw new IllegalArgumentException("missing address");
    }

    try {
      this.address = address;
      this.port = (port == null) ? null : Integer.parseInt(port);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("invalid port");
    }
  }

  protected abstract String getUrl();
}
