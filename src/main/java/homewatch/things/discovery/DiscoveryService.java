package homewatch.things.discovery;

import homewatch.things.Thing;
import homewatch.things.ThingService;

import java.util.List;

interface DiscoveryService<T extends Thing> {
  List<ThingService<T>> perform();
}
