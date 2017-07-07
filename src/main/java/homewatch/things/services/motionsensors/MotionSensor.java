package homewatch.things.services.motionsensors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MotionSensor {
  @JsonProperty
  private final boolean movement;

  public MotionSensor() {
    this.movement = false;
  }

  public MotionSensor(boolean locked) {
    this.movement = locked;
  }

  public boolean hasMovement() {
    return movement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MotionSensor motionsensor = (MotionSensor) o;

    return movement == motionsensor.movement;
  }
}
