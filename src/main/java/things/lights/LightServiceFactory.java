package things.lights;

import exceptions.InvalidSubTypeException;
import things.HttpThingService;
import things.HttpThingServiceFactory;

import java.net.InetAddress;


public class LightServiceFactory implements HttpThingServiceFactory<Light> {
  private LightServiceFactory() {
  }

  public HttpThingService<Light> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLightService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    switch (subtype) {
      case "rest":
        return true;
      default:
        return false;
    }
  }
}
