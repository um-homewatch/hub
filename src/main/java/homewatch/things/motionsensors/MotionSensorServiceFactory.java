package homewatch.things.motionsensors;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.HttpThingService;
import homewatch.things.NetworkThingService;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.ThingService;

import java.net.InetAddress;

public class MotionSensorServiceFactory implements NetworkThingServiceFactory<MotionSensor> {
  @Override
  public NetworkThingService<MotionSensor> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestMotionSensorService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }

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
