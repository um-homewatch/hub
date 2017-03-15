package things.locks;

import exceptions.NetworkException;
import things.Thing;

public interface Lock extends Thing {
  void setLock(boolean isLocked) throws NetworkException;

  boolean isLocked() throws NetworkException;

  default String getType() {
    return "lock";
  }
}
