package homewatch.things.weather;

import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.ThingServiceFactory;

import java.util.logging.Logger;

public class WeatherServiceFactory implements ThingServiceFactory<Weather> {
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
  public boolean isSubType(String subtype) {
    String subTypeUpper = subtype.toUpperCase();

    try {
      SubType.valueOf(subTypeUpper);

      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}

enum SubType {
  REST, OWM
}
