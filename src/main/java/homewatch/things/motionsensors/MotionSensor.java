package homewatch.things.motionsensors;

public class MotionSensor {
  private final boolean moving;

  public MotionSensor() {
    this.moving = false;
  }

  public MotionSensor(boolean locked) {
    this.moving = locked;
  }

  public boolean isMoving() {
    return moving;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MotionSensor motionsensor = (MotionSensor) o;

    return moving == motionsensor.moving;
  }
}
