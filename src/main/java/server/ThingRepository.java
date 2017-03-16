package server;

import things.ThingService;

import java.util.HashMap;
import java.util.Map;

class ThingRepository {
  private static final Map<String, ThingService> THINGS = new HashMap<>();

  public static void addThing(String name, ThingService thingService) {
    THINGS.put(name, thingService);
  }

  public static ThingService getThing(String name) {
    return THINGS.get(name);
  }
}
