package homewatch.things;

import homewatch.exceptions.InvalidSubTypeException;

public interface ThingServiceFactory<T extends Thing> {
  ThingService<T> create(String subtype) throws InvalidSubTypeException;

  boolean isSubType(String subtype);
}
