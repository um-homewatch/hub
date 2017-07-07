package homewatch.things;

import homewatch.exceptions.InvalidSubTypeException;

import java.net.InetAddress;

public interface NetworkThingServiceFactory<T> extends ThingServiceFactory<T> {
  NetworkThingService<T> create(String address, Integer port, String subtype) throws InvalidSubTypeException;
}
