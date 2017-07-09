package homewatch.things;

import homewatch.exceptions.InvalidSubTypeException;

public interface NetworkThingServiceFactory<T extends Thing> extends ThingServiceFactory<T> {
  NetworkThingService<T> create(String address, Integer port, String subtype) throws InvalidSubTypeException;
}
