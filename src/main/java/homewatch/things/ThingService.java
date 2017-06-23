package homewatch.things;

import homewatch.exceptions.NetworkException;

import java.util.Map;

public abstract class ThingService<T> {
  public abstract T get() throws NetworkException;

  public abstract T put(T t) throws NetworkException;

  public abstract boolean ping();

  public abstract void setAttributes(Map<String, ? extends Object> attributes);

  public abstract String getType();

  public abstract String getSubtype();

  protected <NewClass extends Object> NewClass getAttribute(Map<String, ? extends Object> attributes, String attributeName, Class<NewClass> clazz) {
    try {
      Object object = attributes.get(attributeName);

      if (object == null) return null;

      NewClass castedObject = clazz.cast(object);

      return castedObject;
    } catch (ClassCastException ex) {
      throw new IllegalArgumentException(attributeName + " type is invalid, must be " + clazz.getTypeName());
    }
  }
}
