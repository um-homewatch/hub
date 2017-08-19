package homewatch.things.discovery;

import homewatch.things.Attributed;
import homewatch.things.Thing;
import homewatch.things.ThingService;

import java.util.List;

public abstract class DiscoveryService<T extends Thing> extends Attributed {
  public abstract List<ThingService<T>> perform();
}
