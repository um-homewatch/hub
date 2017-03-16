package things.locks;

import exceptions.NetworkException;
import things.ThingService;

public interface Lock extends ThingService {
  void setLock(boolean isLocked) throws NetworkException;

  boolean isLocked() throws NetworkException;

  default String getType() {
    return "lock";
  }
}
