package things;

import exceptions.InvalidSubTypeException;

import java.net.InetAddress;

public interface HttpThingServiceFactory<T> extends ThingServiceFactory<T>{
  HttpThingService<T> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException;
}
