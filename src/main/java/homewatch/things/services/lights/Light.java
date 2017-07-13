package homewatch.things.services.lights;

import homewatch.things.Thing;
import homewatch.things.ThingServiceFactory;

public class Light implements Thing {
  private final boolean on;

  public Light() {
    this.on = false;
  }

  public Light(boolean on) {
    this.on = on;
  }

  public boolean isOn() {
    return on;
  }

  @Override
  public ThingServiceFactory<Light> getFactory() {
    return new LightServiceFactory();
  }

  @Override
  public String getStringRepresentation() {
    return "lights";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Light light = (Light) o;

    return on == light.on;
  }

  @Override
  public int hashCode() {
    return (on ? 1 : 0);
  }

  @Override
  public String toString() {
    return "Light{" +
        "on=" + on +
        '}';
  }
}
