package homewatch.things;

import homewatch.exceptions.NetworkException;

import java.util.Map;

public abstract class ThingService<T extends Thing> {
  public abstract T get() throws NetworkException;

  public abstract T put(T t) throws NetworkException;

  public abstract boolean ping();

  public abstract void setAttributes(Map<String, ?> attributes);

  public abstract String getType();

  public abstract String getSubtype();

  protected <C> C getAttribute(Map<String, ?> attributes, String attributeName, Class<C> clazz) {
    try {
      Object object = attributes.get(attributeName);

      if (object == null) return null;

      return clazz.cast(object);
    } catch (ClassCastException ex) {
      throw new IllegalArgumentException(attributeName + " type is invalid, must be " + clazz.getTypeName());
    }
  }
}
