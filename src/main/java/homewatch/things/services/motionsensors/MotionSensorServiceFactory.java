package homewatch.things.services.motionsensors;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.discovery.NetworkThingDiscoveryService;
import homewatch.things.services.locks.Lock;

public class MotionSensorServiceFactory implements ThingServiceFactory<MotionSensor> {
  @Override
  public ThingService<MotionSensor> createThingService(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestMotionSensorService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public DiscoveryService<MotionSensor> createDiscoveryService(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
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
  REST
}
