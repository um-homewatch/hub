package server;

import things.Thing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joses on 06/03/2017.
 */
public class ThingRepository {
  private static Map<String, Thing> THINGS = new HashMap<>();

  public static void addThing(String name, Thing thing){
    THINGS.put(name, thing);
  }

  public static Thing getThing(String name){
    return THINGS.get(name);
  }
}
