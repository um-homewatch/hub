package homewatch.things;

import homewatch.exceptions.InvalidSubTypeException;

import java.net.InetAddress;

public interface NetworkThingServiceFactory<T> extends ThingServiceFactory<T> {
  <N extends NetworkThingService<T>> N create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException;
}
