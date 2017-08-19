package homewatch.things.services.thermostat;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.discovery.NetworkThingDiscoveryService;
import homewatch.things.services.locks.Lock;
import homewatch.things.services.motionsensors.MotionSensor;


public class ThermostatServiceFactory implements ThingServiceFactory<Thermostat> {
  @Override
  public ThingService<Thermostat> createThingService(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestThermostatService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public DiscoveryService<Thermostat> createDiscoveryService(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new NetworkThingDiscoveryService<>(this, subtype);
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