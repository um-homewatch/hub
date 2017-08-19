package homewatch.things;

import java.util.Map;

public abstract class Attributed {
  public abstract void setAttributes(Map<String, ?> attributes);

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
