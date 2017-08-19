package homewatch.things.discovery;

import homewatch.things.Attributed;
import homewatch.things.Thing;
import homewatch.things.ThingService;

import javax.print.attribute.Attribute;
import java.util.List;
import java.util.Map;

public abstract class DiscoveryService<T extends Thing> extends Attributed {
  public abstract List<ThingService<T>> perform();
}
