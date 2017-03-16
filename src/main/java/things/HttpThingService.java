package things;

import java.net.InetAddress;

public abstract class HttpThingService implements ThingService {
  private InetAddress ipAddress;
  private Integer port = null;

  protected HttpThingService(InetAddress ipAddress) {
    this.ipAddress = ipAddress;
  }

  protected HttpThingService(InetAddress ipAddress, int port) {
    this.ipAddress = ipAddress;
    this.port = port;
  }

  public InetAddress getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(InetAddress ipAddress) {
    this.ipAddress = ipAddress;
  }

  public abstract boolean ping();

  protected String getUrl() {
    if (port == null)
      return String.format("http://%s", this.ipAddress.getHostAddress());
    else
      return String.format("http://%s:%d", this.ipAddress.getHostAddress(), this.port);
  }
}
