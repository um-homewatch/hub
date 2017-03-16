package things.locks;

import exceptions.InvalidSubTypeException;
import things.ThingService;

import java.net.InetAddress;

public class LockServiceFactory {
  public static ThingService<Lock> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLockService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }
}
