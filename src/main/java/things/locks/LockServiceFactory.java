package things.locks;

import exceptions.InvalidSubTypeException;
import things.HttpThingService;
import things.HttpThingServiceFactory;
import java.net.InetAddress;

public class LockServiceFactory implements HttpThingServiceFactory<Lock>{
  public HttpThingService<Lock> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLockService(address, port);
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
