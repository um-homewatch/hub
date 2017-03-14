package things;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A thing that communicates using the IP protocol.
 */
public abstract class HttpThing implements Thing {
  protected InetAddress ipAddress;
  protected Integer port = null;

  public HttpThing(InetAddress ipAddress) {
    this.ipAddress = ipAddress;
  }

  public HttpThing(InetAddress ipAddress, int port) {
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

  protected String getUrl(){
    if (port == null)
      return String.format("http://%s",this.ipAddress.getHostAddress());
    else
      return String.format("http://%s:%d",this.ipAddress.getHostAddress(), this.port);
  }
}
