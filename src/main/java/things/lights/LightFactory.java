package things.lights;

import things.exceptions.InvalidSubTypeException;

import java.net.InetAddress;

public class LightFactory {
  public static Light create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLight(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }
}
