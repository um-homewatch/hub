package homewatch.things;

import java.net.InetAddress;

public abstract class CoapThingService<T> extends NetworkThingService<T> {
  protected CoapThingService() {
  }

  protected CoapThingService(String address) {
    super(address);
  }

  protected CoapThingService(String address, Integer port) {
    super(address, port);
  }

  protected String getUrl() {
    if (this.getPort() == null)
      return String.format("coap://%s", this.getAddress());
    else
      return String.format("coap://%s:%d", this.getAddress(), this.getPort());
  }
}
