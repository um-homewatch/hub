package homewatch.things.services.locks;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.ThingService;

public class LockServiceFactory implements NetworkThingServiceFactory<Lock> {
  @Override
  public NetworkThingService<Lock> create(String address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLockService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public ThingService<Lock> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLockService();
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
