package things.locks;

import things.exceptions.InvalidSubTypeException;

import java.net.InetAddress;

public class LockFactory {
  public static Lock create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestLock(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }
}
