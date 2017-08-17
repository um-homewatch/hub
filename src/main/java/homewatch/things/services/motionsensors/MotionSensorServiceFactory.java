package homewatch.things.services.motionsensors;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;

public class MotionSensorServiceFactory implements ThingServiceFactory<MotionSensor> {
  @Override
  public ThingService<MotionSensor> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestMotionSensorService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    String subtypeUpper = subtype.toUpperCase();
    try {
      SubType.valueOf(subtypeUpper);

      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}

enum SubType {
  REST
}
