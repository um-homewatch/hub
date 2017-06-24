package homewatch.things;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public abstract class CoapThingService<T> extends NetworkThingService<T> {
  public CoapThingService() {
  }

  public CoapThingService(InetAddress address) {
    super(address);
  }

  public CoapThingService(InetAddress address, Integer port) {
    super(address, port);
  }

  protected String getUrl() {
    if (this.getPort() == null)
      return String.format("coap://%s", this.getAddress().getHostAddress());
    else
      return String.format("coap://%s:%d", this.getAddress().getHostAddress(), this.getPort());
  }
}
