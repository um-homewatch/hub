package homewatch.things;

import homewatch.exceptions.NetworkException;

import javax.management.Attribute;
import java.util.Map;

public abstract class ThingService<T extends Thing> extends Attributed{
  public abstract T get() throws NetworkException;

  public abstract T put(T t) throws NetworkException;

  public abstract boolean ping();

  public abstract String getType();

  public abstract String getSubtype();
}
