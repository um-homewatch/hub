package homewatch.things;

import homewatch.exceptions.NetworkException;

public interface ThingService<T> {
  T get() throws NetworkException;

  T put(T t) throws NetworkException;

  boolean ping();

  String getType();

  String getSubType();
}
