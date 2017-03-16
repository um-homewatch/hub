package things;

import exceptions.NetworkException;

public interface ThingService<T> {
  T get() throws NetworkException;

  void put(T t) throws NetworkException;

  boolean ping();

  String getType();

  String getSubType();
}
