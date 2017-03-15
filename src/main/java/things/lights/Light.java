package things.lights;

import exceptions.NetworkException;
import things.Thing;

public interface Light extends Thing {
  void setStatus(boolean status) throws NetworkException;

  boolean getStatus() throws NetworkException;

  default String getType() {
    return "light";
  }
}
