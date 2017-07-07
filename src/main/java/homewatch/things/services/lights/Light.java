package homewatch.things.services.lights;

public class Light {
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
}
