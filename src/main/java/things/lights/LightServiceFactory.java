package things.lights;

import exceptions.InvalidSubTypeException;
import things.ThingService;

import java.net.InetAddress;

public class LightServiceFactory {
  public static ThingService<Light> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLightService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }
}
