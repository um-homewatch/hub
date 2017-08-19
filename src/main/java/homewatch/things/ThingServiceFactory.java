package homewatch.things;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.discovery.DiscoveryService;

public interface ThingServiceFactory<T extends Thing> {
  ThingService<T> createThingService(String subtype) throws InvalidSubTypeException;

  DiscoveryService<T> createDiscoveryService(String subtype) throws InvalidSubTypeException;

  boolean isSubType(String subtype);
}
