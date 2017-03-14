package things.lights;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by joses on 08/03/2017.
 */
public class LightFactory {
  public static Light create(InetAddress address, Integer port, String subtype) throws UnknownHostException {
    switch (subtype){
      case "rest":
        return new RestLight(address, port);
      default:
        return null;
    }
  }
}
