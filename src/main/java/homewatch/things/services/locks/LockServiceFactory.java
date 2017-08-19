package homewatch.things.services.locks;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.discovery.NetworkThingDiscoveryService;

public class LockServiceFactory implements ThingServiceFactory<Lock> {
  @Override
  public ThingService<Lock> createThingService(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLockService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public DiscoveryService<Lock> createDiscoveryService(String subtype) throws InvalidSubTypeException {
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
