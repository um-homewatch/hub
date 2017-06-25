package homewatch.things;

import java.net.InetAddress;

public abstract class CoapThingService<T> extends NetworkThingService<T> {
  protected CoapThingService() {
  }

  protected CoapThingService(InetAddress address) {
    super(address);
  }

  protected CoapThingService(InetAddress address, Integer port) {
    super(address, port);
  }

  protected String getUrl() {
    if (this.getPort() == null)
      return String.format("coap://%s", this.getAddress().getHostAddress());
    else
      return String.format("coap://%s:%d", this.getAddress().getHostAddress(), this.getPort());
  }
}
