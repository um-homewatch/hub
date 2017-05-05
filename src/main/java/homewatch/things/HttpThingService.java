package homewatch.things;

import java.net.InetAddress;

public abstract class HttpThingService<T> implements ThingService<T> {
  private InetAddress address;
  private Integer port = null;

  protected HttpThingService() {
  }

  protected HttpThingService(InetAddress address) {
    this.address = address;
  }

  protected HttpThingService(InetAddress address, Integer port) {
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

  protected String getUrl() {
    if (port == null)
      return String.format("http://%s", this.address.getHostAddress());
    else
      return String.format("http://%s:%d", this.address.getHostAddress(), this.port);
  }
}
