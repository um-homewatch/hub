package things.lights;

import things.Thing;
import things.exceptions.NetworkException;

/**
 * Created by joses on 22/02/2017.
 */
public interface Light extends Thing {
  void setStatus(boolean status) throws NetworkException;

  boolean getStatus() throws NetworkException;

  default String getType(){
    return "lights";
  }
}
