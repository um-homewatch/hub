package things.locks;

import things.Thing;
import things.exceptions.NetworkException;

public interface Lock extends Thing {
  void setLock(boolean isLocked) throws NetworkException;

  boolean isLocked() throws NetworkException;

  default String getType() {
    return "locks";
  }
}
