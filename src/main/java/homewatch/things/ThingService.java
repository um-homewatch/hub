package homewatch.things;

import homewatch.exceptions.NetworkException;

public abstract class ThingService<T extends Thing> extends Attributed {
  public abstract T get() throws NetworkException;

  public abstract T put(T t) throws NetworkException;

  public abstract boolean ping();

  public abstract String getType();

  public abstract String getSubtype();
}
