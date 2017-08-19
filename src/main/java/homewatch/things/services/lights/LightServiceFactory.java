package homewatch.things.services.lights;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.discovery.NetworkThingDiscoveryService;


public class LightServiceFactory implements ThingServiceFactory<Light> {
  @Override
  public ThingService<Light> createThingService(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "hue":
        return new HueLightService();
      case "rest":
        return new RestLightService();
      case "coap":
        return new CoapLightService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public DiscoveryService<Light> createDiscoveryService(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "hue":
        return new NetworkThingDiscoveryService<>(this, subtype);
      case "rest":
        return new NetworkThingDiscoveryService<>(this, subtype);
      case "coap":
        return new NetworkThingDiscoveryService<>(this, subtype);
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    String subtypeUpper = subtype.toUpperCase();
    try {
      SubType.valueOf(subtypeUpper);

      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}

enum SubType {
  REST, HUE, COAP
}