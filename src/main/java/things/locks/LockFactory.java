package things.locks;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by joses on 08/03/2017.
 */
public class LockFactory {
  public static Lock create(InetAddress address, Integer port, String subtype) throws UnknownHostException {
    switch (subtype) {
      case "rest":
          return new RestLock(address, port);
      default:
        return null;
    }
  }
}
