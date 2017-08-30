package homewatch.things.discovery;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.Attributed;
import homewatch.things.Thing;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;

import java.util.List;

public abstract class DiscoveryService<T extends Thing> extends Attributed {
  protected ThingServiceFactory<T> thingServiceFactory;
  protected final String subtype;

  public DiscoveryService(ThingServiceFactory<T> thingServiceFactory, String subtype) throws InvalidSubTypeException {
    if (!thingServiceFactory.isSubType(subtype))
      throw new InvalidSubTypeException();

    this.thingServiceFactory = thingServiceFactory;
    this.subtype = subtype;
  }

  public abstract List<ThingService<T>> perform();
}
