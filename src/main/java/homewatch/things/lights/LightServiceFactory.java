package homewatch.things.lights;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.HttpThingService;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;

import java.net.InetAddress;


public class LightServiceFactory implements HttpThingServiceFactory<Light> {
  @Override
  public HttpThingService<Light> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLightService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public ThingService<Light> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLightService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    String subTypeUpper = subtype.toUpperCase();
    try {
      SubType.valueOf(subTypeUpper);

      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}

enum SubType {
  REST
}