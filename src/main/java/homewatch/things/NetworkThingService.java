package homewatch.things;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public abstract class NetworkThingService<T> extends ThingService<T> {
  private InetAddress address;
  private Integer port = null;

  NetworkThingService() {
  }

  NetworkThingService(InetAddress address) {
    this.address = address;
  }

  NetworkThingService(InetAddress address, Integer port) {
    this.address = address;
    this.port = port;
  }

  public InetAddress getAddress() {
    return address;
  }

  public void setAddress(InetAddress address) {
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
      this.address = InetAddress.getByName(address);
      this.port = (port == null) ? null : Integer.parseInt(port);
    } catch (UnknownHostException ex) {
      throw new IllegalArgumentException(ex);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("invalid port");
    }
  }

  protected abstract String getUrl();
}
