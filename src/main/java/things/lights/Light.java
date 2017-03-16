package things.lights;

import exceptions.NetworkException;
import things.ThingService;

public interface Light extends ThingService {
  void setStatus(boolean status) throws NetworkException;

  boolean getStatus() throws NetworkException;

  default String getType() {
    return "light";
  }
}
