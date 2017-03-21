package things;

import exceptions.InvalidSubTypeException;

public interface ThingServiceFactory<T> {
  ThingService<T> create(String subtype) throws InvalidSubTypeException;

  boolean isSubType(String subtype);
}
