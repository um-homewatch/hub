package homewatch.things.weather;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.ThingService;

import java.net.InetAddress;

public class WeatherServiceFactory implements NetworkThingServiceFactory<Weather> {
  @Override
  public ThingService<Weather> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "owm":
        return new OWMWeatherService();
      case "rest":
        return new RestWeatherService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public NetworkThingService<Weather> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestWeatherService(address, port);
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
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}

enum SubType {
  REST, OWM
}
