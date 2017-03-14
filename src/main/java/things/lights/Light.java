package things.lights;

import things.Thing;
import things.exceptions.NetworkException;

public interface Light extends Thing {
  void setStatus(boolean status) throws NetworkException;

  boolean getStatus() throws NetworkException;

  default String getType() {
    return "lights";
  }
}
