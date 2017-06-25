package homewatch.things;

import java.net.InetAddress;

public abstract class HttpThingService<T> extends NetworkThingService<T> {
  protected HttpThingService() {
  }

  protected HttpThingService(InetAddress address) {
    super(address);
  }

  protected HttpThingService(InetAddress address, Integer port) {
    super(address, port);
  }

  protected String getUrl() {
    if (this.getPort() == null)
      return String.format("http://%s", this.getAddress().getHostAddress());
    else
      return String.format("http://%s:%d", this.getAddress().getHostAddress(), this.getPort());
  }
}
